package com.alibaba.fastjson2;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * A JSONReader that extends {@link JSONReaderUTF8} to track the exact byte offset
 * where a parsed JSON object ends. Designed for non-greedy parsing: when the
 * input contains trailing non-JSON content after a valid object, this reader
 * captures the precise boundary.
 *
 * <p>This class lives in the {@code com.alibaba.fastjson2} package because
 * {@code JSONReaderUTF8} is package-private. It does not modify any existing
 * source files.</p>
 *
 * <p>Supported features (passed through to the underlying reader):</p>
 * <ul>
 *   <li>{@link Feature#AllowUnQuotedFieldNames} — unquoted keys like {@code {key:"val"}}</li>
 *   <li>{@link Feature#IgnoreCheckClose} — no-op on the readObject() path; trailing content is naturally ignored</li>
 *   <li>{@link Feature#NonStringKeyAsString} — numeric keys are handled as unquoted names when AllowUnQuotedFieldNames is set</li>
 * </ul>
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * OffsetResult r = OffsetJSONReader.parseObject(
 *     "{\"test\":\"111\"} trailing garbage",
 *     Feature.AllowUnQuotedFieldNames,
 *     Feature.IgnoreCheckClose,
 *     Feature.NonStringKeyAsString
 * );
 * Map<String, Object> map = r.object;  // {"test":"111"}
 * int endOffset = r.endOffset;         // byte position right after '}'
 * }</pre>
 */
public class OffsetJSONReader extends JSONReaderUTF8 {
    /**
     * The byte offset immediately after the closing '}' of the most recently
     * parsed JSON object. Set to -1 before any object has been parsed.
     *
     * <p>For nested objects this value is overwritten — the outermost '}'
     * position is the final value.</p>
     */
    private int jsonEndOffset = -1;

    /**
     * Constructs an OffsetJSONReader over a byte range.
     *
     * @param ctx    the reader context (features, provider, etc.)
     * @param bytes  the UTF-8 encoded input
     * @param offset start offset in the array
     * @param length number of bytes to read
     */
    public OffsetJSONReader(Context ctx, byte[] bytes, int offset, int length) {
        super(ctx, bytes, offset, length);
    }

    /**
     * Intercepts {@link #next()} to capture the byte offset at the exact
     * moment a JSON object's closing '}' is detected.
     *
     * <p>The core insight: in {@code JSONReader.readObject()} (line 3719),
     * when {@code ch == '}'} the reader calls {@code next()} to advance
     * past the brace. At that instant {@code this.offset} points to the
     * byte immediately following '}' — the precise JSON end boundary.
     * We save this value before {@code super.next()} overwrites it with
     * the lookahead position.</p>
     */
    @Override
    public void next() {
        char prevCh = this.ch;
        int prevOffset = this.offset;
        super.next();
        if (prevCh == '}') {
            this.jsonEndOffset = prevOffset;
        }
    }

    /**
     * Returns the byte offset immediately after the closing '}' of the
     * most recently parsed top-level JSON object, or -1 if no object
     * has been parsed yet.
     */
    public int getJsonEndOffset() {
        return jsonEndOffset;
    }

    // ──────────────────────────────────────────────
    // Static convenience methods
    // ──────────────────────────────────────────────

    /**
     * Non-greedy JSON object parse over the full byte array.
     *
     * @param utf8Bytes UTF-8 encoded JSON text (trailing content allowed)
     * @param features  optional reader features
     * @return the parsed map and the byte offset where JSON ended
     */
    public static OffsetResult parseObject(byte[] utf8Bytes, Feature... features) {
        return parseObject(utf8Bytes, 0, utf8Bytes.length, features);
    }

    /**
     * Non-greedy JSON object parse over a byte range.
     *
     * @param utf8Bytes UTF-8 encoded JSON text (trailing content allowed)
     * @param off       start offset
     * @param len       number of bytes to read
     * @param features  optional reader features
     * @return the parsed map and the byte offset where JSON ended
     */
    public static OffsetResult parseObject(byte[] utf8Bytes, int off, int len, Feature... features) {
        Context ctx = JSONFactory.createReadContext(features);
        try (OffsetJSONReader reader = new OffsetJSONReader(ctx, utf8Bytes, off, len)) {
            Map<String, Object> map = reader.readObject();
            return new OffsetResult(map, reader.getJsonEndOffset());
        }
    }

    /**
     * Non-greedy JSON object parse from a String. The string is converted
     * to UTF-8 bytes internally; the returned {@code endOffset} is a byte
     * offset within that UTF-8 representation.
     *
     * @param json     the JSON string (trailing content allowed)
     * @param features optional reader features
     * @return the parsed map and the byte offset where JSON ended
     */
    public static OffsetResult parseObject(String json, Feature... features) {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        return parseObject(bytes, 0, bytes.length, features);
    }

    // ──────────────────────────────────────────────
    // Result holder
    // ──────────────────────────────────────────────

    /**
     * Holds the result of a non-greedy JSON object parse.
     */
    public static class OffsetResult {
        /**
         * The parsed JSON object as a Map.
         */
        public final Map<String, Object> object;

        /**
         * Byte offset immediately after the closing '}' of the parsed JSON object.
         * The remaining input (if any) starts at this offset.
         */
        public final int endOffset;

        public OffsetResult(Map<String, Object> object, int endOffset) {
            this.object = object;
            this.endOffset = endOffset;
        }
    }
}

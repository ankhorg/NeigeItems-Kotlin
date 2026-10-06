package pers.neige.neigeitems.action.evaluator;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public class EvaluatorConverter<T> {
    protected final @NonNull Class<T> type;

    /**
     * 尝试将 String 转换成对应类型.
     * 这一步是最后进行的, 用于将 Evaluator 最后返回的内容解析为目标结果.
     */
    public @Nullable T convert(@NonNull String input) {
        return convert((Object) input);
    }

    /**
     * 尝试将 String 进行预解析.
     * 这一步是编译时执行的, 如果有返回值, 代表这段文本已经是符合格式的内容了, 会直接缓存起来留待后续使用.
     * 如果没有返回值, 代表这段文本可能需要在后续环境中进行节点解析.
     * 对于 Integer 这类结果, 无需过多思考, 直接尝试转换传入文本即可.
     * 对于 String 这类结果, 需要通过检测是否包含 \< 或 \> 的方式判断文本是否需要留待后续解析.
     */
    public @Nullable T parseStaticValue(@NonNull String input) {
        return convert(input);
    }

    public @Nullable T convert(@NonNull Object input) {
        if (type.isInstance(input)) {
            return type.cast(input);
        }
        return null;
    }
}

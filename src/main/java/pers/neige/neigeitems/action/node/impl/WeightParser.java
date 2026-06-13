package pers.neige.neigeitems.action.node.impl;

import lombok.NonNull;
import lombok.val;
import org.jetbrains.annotations.Nullable;
import pers.neige.neigeitems.action.ActionContext;
import pers.neige.neigeitems.action.node.NodeParser;
import pers.neige.neigeitems.config.ConfigReader;
import pers.neige.neigeitems.manager.BaseActionManager;
import pers.neige.neigeitems.utils.NumberParser;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class WeightParser extends NodeParser {
    public WeightParser(@NonNull BaseActionManager manager) {
        super(manager);
    }

    @Override
    public @NonNull String getId() {
        return "weight";
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull ConfigReader params
    ) {
        return handle(
            params.getStringList("values").stream().map(it -> this.manager.parseNode(it, context)).collect(Collectors.toList())
        );
    }

    @Override
    public @Nullable String parse(
        @NonNull ActionContext context,
        @NonNull List<String> params
    ) {
        return handle(
            params
        );
    }

    private @Nullable String handle(
        @Nullable List<String> values
    ) {
        if (values == null || values.isEmpty()) return null;
        val info = new HashMap<String, BigDecimal>();
        BigDecimal total = BigDecimal.ZERO;
        // 加载所有参数并遍历
        for (val value : values) {
            // 检测权重
            val index = value.indexOf("::");
            // 无权重, 直接记录
            if (index == -1) {
                info.put(value, info.getOrDefault(value, BigDecimal.ZERO).add(BigDecimal.ONE));
                total = total.add(BigDecimal.ONE);
            } else {
                // 有权重, 根据权重大小进行记录
                val weight = NumberParser.parseBigDecimal(value.substring(0, index), BigDecimal.ONE);
                val string = value.substring(index + 2);
                info.put(string, info.getOrDefault(string, BigDecimal.ZERO).add(weight));
                total = total.add(weight);
            }
        }
        val random = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble()).multiply(total);
        BigDecimal current = BigDecimal.ZERO;
        for (val entry : info.entrySet()) {
            current = current.add(entry.getValue());
            if (random.compareTo(current) <= 0) {
                return entry.getKey();
            }
        }
        return null;
    }
}

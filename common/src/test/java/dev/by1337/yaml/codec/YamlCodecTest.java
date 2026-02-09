package dev.by1337.yaml.codec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

public class YamlCodecTest {

    @Test
    public void run() {
        RecursiveTest test = new RecursiveTest("1", new RecursiveTest("2", null));

        System.out.println(RecursiveTest.CODEC2.encode(test).asYamlMap().result());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(
                RecursiveTest.CODEC2.schema().or(RecursiveTest.CODEC2.schema()
                ).buildJson()));
        System.out.println(gson.toJson(YamlCodec.STRING.schema().buildJson()));
    }

    private static record RecursiveTest(String payload, @Nullable RecursiveTest node) {
        public static final YamlCodec<RecursiveTest> CODEC = RecordYamlCodecBuilder.mapOf(
                RecursiveTest::new,
                YamlCodec.STRING.fieldOf("payload", RecursiveTest::payload),
                YamlCodec.lazyLoad(() -> {
                            return recursive();
                        })
                        .fieldOf("node", RecursiveTest::node)
        );

        public static YamlCodec<RecursiveTest> recursive() {
            return CODEC;
        }

        public static final YamlCodec<RecursiveTest> CODEC2 = YamlCodec.recursive(codec -> {
            return RecordYamlCodecBuilder.mapOf(
                    RecursiveTest::new,
                    YamlCodec.STRING.fieldOf("payload", RecursiveTest::payload),
                    codec.fieldOf("node", RecursiveTest::node)
            );
        });
    }
}
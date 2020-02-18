package application.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.util.Pair;

import java.util.stream.IntStream;

public class Encode {
    public Image encode(Image image, String message) {
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        WritableImage copy = new WritableImage(image.getPixelReader(), width, height);
        PixelWriter writer = copy.getPixelWriter();
        PixelReader reader = image.getPixelReader();

        boolean[] bits = encode(message);

//        for (int i=0; i<bits.length; i++){
//            System.out.println(reader.getArgb(i % width, i / width));
//        }
        IntStream.range(0, bits.length)
                .mapToObj(i -> new Pair<>(i, reader.getArgb(i % width, i / width)))
                .map(pair -> new Pair<>(pair.getKey(), bits[pair.getKey()] ? pair.getValue() | 1 : pair.getValue() & ~1))
                .forEach(pair -> {
                    int x = pair.getKey() % width;
                    int y = pair.getKey() / width;
                    writer.setArgb(x, y, pair.getValue());
                    System.out.println("old: " + Integer.toBinaryString(reader.getArgb(x, y)));
                    System.out.println("new: " + Integer.toBinaryString(pair.getValue()));
                });
        return copy;
    }

    public boolean[] encode(String message) {
        byte[] data = message.getBytes();

        boolean[] bits = new boolean[32 + data.length * 8];

        String binary = Integer.toBinaryString(data.length);
        while (binary.length() < 32) {
            binary = "0" + binary;
        }

        for (int i = 0; i < 32; i++) {
            bits[i] = binary.charAt(i) == '1';
        }

        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            for (int j = 0; j < 8; j++) {
                bits[32 + i * 8 + j] = ((b >> (7 - j)) & 1) == 1;
            }
        }
        return bits;
    }
}

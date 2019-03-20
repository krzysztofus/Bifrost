package org.krzysztofus.bifrost;

import java.io.IOException;

/**
 * Created by krzysztofus on 3/20/2019.
 */
public class App {

    public static void main(String[] args) throws IOException {
        final Bifrost bifrost = new Bifrost();
        bifrost.execute();
    }
}

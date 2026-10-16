package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ServicioEnVentanaTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static ServicioEnVentana getServicioEnVentanaSample1() {
        return new ServicioEnVentana().id(1L).notas("notas1");
    }

    public static ServicioEnVentana getServicioEnVentanaSample2() {
        return new ServicioEnVentana().id(2L).notas("notas2");
    }

    public static ServicioEnVentana getServicioEnVentanaRandomSampleGenerator() {
        return new ServicioEnVentana().id(longCount.incrementAndGet()).notas(UUID.randomUUID().toString());
    }
}

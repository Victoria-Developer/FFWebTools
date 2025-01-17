package app;

import app.dto.Coordinate;
import app.dto.Point;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})

public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        /*Coordinate t1 = new Coordinate("T1", new Point(13.3, 31.1));
        Coordinate t2 = new Coordinate("T2", new Point(31.7, 18));
        Coordinate[] teleports = new Coordinate[]{t1, t2};

        Coordinate p1 = new Coordinate("A", new Point(14.9, 26.6));
        Coordinate p2 = new Coordinate("B", new Point(19.9, 9.5));
        Coordinate p3 = new Coordinate("C", new Point(24.0, 20.8));
        Coordinate p4 = new Coordinate("D", new Point(29.4, 29.9));
        Coordinate p5 = new Coordinate("E", new Point(29.8, 12.1));
        Coordinate p6 = new Coordinate("F", new Point(20, 29.9));
        Coordinate p7 = new Coordinate("G", new Point(15, 24));

        List<Coordinate> playerC = new ArrayList<>(List.of(p1, p2, p3, p4, p5, p6, p7));*/
        //new TestTsp().solve(teleports, playerC);
    }
}

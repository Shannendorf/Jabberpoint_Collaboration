package Application;

import Domain.Presentation;
import Domain.Slide;

import java.util.ArrayList;

//Factory pattern correct? Definitely not abstract factory : Tibo
public class PresentationFactory {

    /**
     * Maak een presentatie aan
     */
    public static Presentation createPresentation()
    {
        return new Presentation();
    }
}

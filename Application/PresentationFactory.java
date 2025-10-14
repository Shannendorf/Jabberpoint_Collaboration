package Application;

import Domain.Presentation;
import Domain.Slide;

import java.util.ArrayList;

//Factory pattern correct? Definitely not abstract factory : Tibo
public class PresentationFactory {

    public static Presentation createPresentation()
    {
        return new Presentation();
    }
}

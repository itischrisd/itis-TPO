/**
 *
 *  @author Dudek Krzysztof S25692
 *
 */

package zad2;


public class Main {
  public static void main(String[] args) {
    Service s = new Service("Poland");
    String weatherJson = s.getWeather("Warsaw");
    Double rate1 = s.getRateFor("USD");
    Double rate2 = s.getNBPRate();
    s.displayUserInterface();
  }
}

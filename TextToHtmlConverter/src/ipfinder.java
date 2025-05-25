import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ipfinder {
    public static void main(String[] args) {
        // Create a Scanner object to get URL input from the user
        Scanner scanner = new Scanner(System.in);

        // Ask the user to input a URL
        System.out.print("Enter a URL (with http/https): ");
        String urlString = scanner.nextLine();

        try {
            // Create a URL object from the input string
            URL url = new URL(urlString);

            // Extract the host (domain name) from the URL
            String host = url.getHost();

            // Get the IP address of the host
            InetAddress address = InetAddress.getByName(host);

            // Print the IP address
            System.out.println("IP address of " + host + ": " + address.getHostAddress());
        } catch (MalformedURLException e) {
            // Handle invalid URL format
            System.out.println("Invalid URL format: " + urlString);
        } catch (UnknownHostException e) {
            // Handle case where the IP address could not be found
            System.out.println("Could not find IP address for the URL: " + urlString);
        }

        // Close the scanner
        scanner.close();
    }
}

package com.webforjrest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.webforjrest.entity.Customer;
import com.webforjrest.repository.CustomerRepository;

import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        customerRepository.deleteAll();

        // Generate 100 random customers
        String[] firstNames = {"Sam", "Taylor", "Morgan", "Jordan", "Casey", "Alex", "Jamie", "Riley", "Avery", "Cameron",
                              "John", "Jane", "Bob", "Alice", "Charlie", "Diana", "Ethan", "Fiona", "George", "Hannah",
                              "Michael", "Sarah", "David", "Emma", "Daniel", "Olivia", "Matthew", "Sophia", "Christopher", "Isabella",
                              "Andrew", "Mia", "Joshua", "Emily", "Ryan", "Abigail", "Nicholas", "Madison", "Tyler", "Elizabeth",
                              "Brandon", "Ashley", "Jacob", "Samantha", "Kevin", "Jessica", "Kyle", "Rachel", "Nathan", "Victoria"};
        String[] lastNames = {"Anderson", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Walker", "Hall",
                             "Smith", "Doe", "Johnson", "Williams", "Brown", "Prince", "Hunt", "Green", "Miller", "Lee",
                             "Wilson", "Moore", "Taylor", "Jackson", "White", "Harris", "Martin", "Thomas", "Davis", "Lopez",
                             "Gonzalez", "Hernandez", "Young", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill", "Flores",
                             "Adams", "Nelson", "Baker", "Carter", "Mitchell", "Roberts", "Phillips", "Campbell", "Parker", "Evans"};
        String[] companies = {"Tech Solutions", "Digital Services", "Innovation Labs", "Smart Systems", "Cloud Networks",
                             "Data Dynamics", "Future Corp", "NextGen Tech", "Alpha Industries", "Beta Solutions",
                             "Acme Corp", "TechStart Inc", "Builders United", "Innovate Solutions", "Global Enterprises",
                             "Wonder Tech", "Mission Systems", "Eco Friendly Co", "Media Group", "Design Studio",
                             "Quantum Computing", "Cyber Dynamics", "Peak Performance", "Velocity Systems", "Zenith Labs",
                             "Apex Solutions", "Horizon Technologies", "Nexus Group", "Pinnacle Corp", "Vertex Innovations"};

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String name = firstName + " " + lastName;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@company.com";
            String company = companies[random.nextInt(companies.length)];
            String phone = String.format("555-%04d", i + 100);

            customerRepository.save(new Customer(name, email, company, phone));
        }

        System.out.println("Sample customer data initialized: " + customerRepository.count() + " customers");
    }
}

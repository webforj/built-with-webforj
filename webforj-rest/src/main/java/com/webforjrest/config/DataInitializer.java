package com.webforjrest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.webforjrest.entity.Customer;
import com.webforjrest.repository.CustomerRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        // Clear existing data
        customerRepository.deleteAll();

        // Add sample customers
        customerRepository.save(new Customer("John Smith", "john.smith@example.com", "Acme Corp", "555-0101"));
        customerRepository.save(new Customer("Jane Doe", "jane.doe@techstart.com", "TechStart Inc", "555-0102"));
        customerRepository.save(new Customer("Bob Johnson", "bob.j@builders.com", "Builders United", "555-0103"));
        customerRepository.save(new Customer("Alice Williams", "alice.w@innovate.com", "Innovate Solutions", "555-0104"));
        customerRepository.save(new Customer("Charlie Brown", "charlie.b@global.com", "Global Enterprises", "555-0105"));
        customerRepository.save(new Customer("Diana Prince", "diana.p@wonder.com", "Wonder Tech", "555-0106"));
        customerRepository.save(new Customer("Ethan Hunt", "ethan.h@mission.com", "Mission Systems", "555-0107"));
        customerRepository.save(new Customer("Fiona Green", "fiona.g@eco.com", "Eco Friendly Co", "555-0108"));
        customerRepository.save(new Customer("George Miller", "george.m@media.com", "Media Group", "555-0109"));
        customerRepository.save(new Customer("Hannah Lee", "hannah.l@design.com", "Design Studio", "555-0110"));
        customerRepository.save(new Customer("Ian Martinez", "ian.m@cloud.com", "CloudFirst Technologies", "555-0111"));
        customerRepository.save(new Customer("Julia Roberts", "julia.r@finance.com", "Finance Plus", "555-0112"));
        customerRepository.save(new Customer("Kevin Nash", "kevin.n@logistics.com", "Logistics Pro", "555-0113"));
        customerRepository.save(new Customer("Laura Chen", "laura.c@consulting.com", "Chen Consulting", "555-0114"));
        customerRepository.save(new Customer("Michael Scott", "michael.s@paper.com", "Dunder Mifflin", "555-0115"));
        customerRepository.save(new Customer("Nina Patel", "nina.p@healthcare.com", "HealthCare Plus", "555-0116"));
        customerRepository.save(new Customer("Oscar Wilde", "oscar.w@literary.com", "Literary Works Inc", "555-0117"));
        customerRepository.save(new Customer("Paula Jackson", "paula.j@retail.com", "Retail Solutions", "555-0118"));
        customerRepository.save(new Customer("Quinn Taylor", "quinn.t@sports.com", "Sports Unlimited", "555-0119"));
        customerRepository.save(new Customer("Rachel Adams", "rachel.a@education.com", "Education First", "555-0120"));

        // Generate additional customers to reach 100
        String[] firstNames = {"Sam", "Taylor", "Morgan", "Jordan", "Casey", "Alex", "Jamie", "Riley", "Avery", "Cameron"};
        String[] lastNames = {"Anderson", "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Walker", "Hall"};
        String[] companies = {"Tech Solutions", "Digital Services", "Innovation Labs", "Smart Systems", "Cloud Networks",
                             "Data Dynamics", "Future Corp", "NextGen Tech", "Alpha Industries", "Beta Solutions"};

        for (int i = 21; i <= 100; i++) {
            String firstName = firstNames[(i - 21) % firstNames.length];
            String lastName = lastNames[(i - 21) / firstNames.length % lastNames.length];
            String name = firstName + " " + lastName + " " + i;
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + i + "@company.com";
            String company = companies[(i - 21) % companies.length];
            String phone = String.format("555-%04d", i + 100);

            customerRepository.save(new Customer(name, email, company, phone));
        }

        System.out.println("Sample customer data initialized: " + customerRepository.count() + " customers");
    }
}

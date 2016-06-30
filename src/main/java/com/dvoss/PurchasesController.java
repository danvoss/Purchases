package com.dvoss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by Dan on 6/22/16.
 */
@Controller
public class PurchasesController {
    @Autowired
    CustomerRepository customers;
    @Autowired
    PurchaseRepository purchases;

    @PostConstruct
    public void init() throws FileNotFoundException {
        if (customers.count() == 0) {
            File f = new File("customers.csv");
            Scanner scanner = new Scanner(f);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Customer customer = new Customer(columns[0], columns[1]);
                customers.save(customer);
            }
        }
        if (purchases.count() == 0) {
            File f = new File("purchases.csv");
            Scanner scanner = new Scanner(f);
            scanner.nextLine();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] columns = line.split(",");
                Purchase purchase = new Purchase(columns[1], columns[2], Integer.valueOf(columns[3]), columns[4], customers.findOne(Integer.valueOf(columns[0])));
                purchases.save(purchase);
            }
        }
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String home(Model model, String category, Integer page) {
        page = (page == null) ? 0 : page; // single-line conditional: sets page as 0 if null, otherwise as page number
        PageRequest pr = new PageRequest(page, 10);
        Page<Purchase> p;
        if (category != null) {
            p = purchases.findByCategory(pr, category);
        }
        else {
            p = purchases.findAll(pr);

        }
        model.addAttribute("purchases", p);
        model.addAttribute("category", category);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("showNext", p.hasNext());
        model.addAttribute("prevPage", page - 1);
        model.addAttribute("showPrev", p.hasPrevious());

        return "home";
    }
}

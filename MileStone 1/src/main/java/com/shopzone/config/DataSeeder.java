package com.shopzone.config;

import com.shopzone.model.Product;
import com.shopzone.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() > 0) return; // already seeded

        productRepository.save(product("Wireless Headphones",
            "Premium noise-cancelling over-ear headphones with 30h battery.",
            6599.00, "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400", 15));
        productRepository.save(product("Mechanical Keyboard",
            "Compact TKL keyboard with RGB backlight and tactile switches.",
            4999.00, "https://images.unsplash.com/photo-1618384887929-16ec33fab9ef?w=400", 8));
        productRepository.save(product("USB-C Hub",
            "7-in-1 hub: HDMI 4K, 3x USB-A, SD card, PD charging.",
            2899.00, "https://images.unsplash.com/photo-1587202372775-e229f172b9d7?w=400", 25));
        productRepository.save(product("Webcam 1080p",
            "Full HD webcam with built-in mic and auto light correction.",
            4199.00, "https://images.unsplash.com/photo-1596742578443-7682ef5251cd?w=400", 12));
        productRepository.save(product("Desk Lamp LED",
            "Adjustable colour temperature and brightness, USB charging port.",
            2499.00, "https://images.unsplash.com/photo-1534073828943-f801091bb18c?w=400", 20));
        productRepository.save(product("Mouse Pad XL",
            "Extended gaming mouse pad 900x400mm, stitched edges.",
            1599.00, "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=400", 0));
    }

    private Product product(String name, String desc, double price, String image, int stock) {
        Product p = new Product();
        p.setName(name); p.setDescription(desc);
        p.setPrice(price); p.setImage(image); p.setStock(stock);
        return p;
    }
}

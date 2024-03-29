package id.ac.ui.cs.advprog.eshop.controller;

import id.ac.ui.cs.advprog.eshop.exceptions.*;
import id.ac.ui.cs.advprog.eshop.model.Product;
import id.ac.ui.cs.advprog.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private static final String PRODUCT_ATTR_NAME = "product";
    private static final String ERR_ATTR_NAME = "error";

    private static final String CREATE_PRODUCT = "CreateProduct";
    private static final String PRODUCT_LIST = "ProductList";
    private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";
    private static final String EDIT_PRODUCT = "EditProduct";
    private static final String DELETE_PRODUCT = "DeleteProduct";

    private static final String ILLEGAL_QUANTITY_EXCEPTION_MSG = "Product quantity is not an integer";
    private static final String NEGATIVE_QUANTITY_EXCEPTION_MSG = "Product quantity cannot be negative";
    private static final String ZERO_LENGTH_NAME_EXCEPTION_MSG = "Product name should not be left empty";
    private static final String NULL_NAME_EXCEPTION_MSG = "Request body is invalid";
    private static final String INVALID_ID_MSG = "Invalid product ID";
    private static final String RUNTIME_EXCEPTION_MSG = "An unknown exception has occured";

    private ProductService service;

    @Autowired
    ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping({"/create", "/create/"})
    public String createProductPage(Model model) {
        Product product = new Product();
        model.addAttribute(PRODUCT_ATTR_NAME, product);
        return CREATE_PRODUCT;
    }

    @PostMapping({"/create", "/create/"})
    public String createProductPost(Model model, @ModelAttribute Product product, BindingResult result) {
        try {
            if (result.hasErrors())
                throw new IllegalItemQuantityException(ILLEGAL_QUANTITY_EXCEPTION_MSG);

            service.create(product);

        } catch (IllegalItemQuantityException exception) {

            model.addAttribute(ERR_ATTR_NAME, ILLEGAL_QUANTITY_EXCEPTION_MSG);
            return CREATE_PRODUCT;

        } catch (NegativeItemQuantityException exception) {

            model.addAttribute(ERR_ATTR_NAME, NEGATIVE_QUANTITY_EXCEPTION_MSG);
            return CREATE_PRODUCT;

        } catch (ZeroLengthItemNameException exception) {

            model.addAttribute(ERR_ATTR_NAME, ZERO_LENGTH_NAME_EXCEPTION_MSG);
            return CREATE_PRODUCT;

        } catch (NullItemNameException exception) {

            model.addAttribute(ERR_ATTR_NAME, NULL_NAME_EXCEPTION_MSG);
            return CREATE_PRODUCT;

        } catch (RuntimeException exception) {

            model.addAttribute(ERR_ATTR_NAME, RUNTIME_EXCEPTION_MSG);
            return CREATE_PRODUCT;

        }

        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping({"/list", "/list/"})
    public String productListPage(Model model) {
        List<Product> allProducts = service.findAll();
        model.addAttribute("products", allProducts);
        return PRODUCT_LIST;
    }

    @GetMapping({"/edit", "/edit/", "/edit/{productId}", "/edit/{productId}/"})
    public String editProductPage(Model model, @PathVariable(required=false) String productId) {
        Product product;
        try {
            product = service.findById(productId);
        } catch (RuntimeException exception) {
            return REDIRECT_PRODUCT_LIST;
        }

        model.addAttribute(PRODUCT_ATTR_NAME, product);
        return EDIT_PRODUCT;
    }

    @PostMapping({"/edit", "/edit/", "/edit/{productId}", "/edit/{productId}/"})
    public String editProductPost(Model model, @PathVariable(required=false) String productId, @ModelAttribute Product product, BindingResult result) {

        product.setProductId(productId);

        try {
            if (result.hasErrors())
                throw new IllegalItemQuantityException(ILLEGAL_QUANTITY_EXCEPTION_MSG);

            service.update(product);

        } catch (IllegalItemQuantityException exception) {

            model.addAttribute(ERR_ATTR_NAME, ILLEGAL_QUANTITY_EXCEPTION_MSG);
            return EDIT_PRODUCT;

        } catch (ItemNotFoundException | ZeroLengthItemIdException exception) {

            model.addAttribute(ERR_ATTR_NAME, INVALID_ID_MSG);
            return EDIT_PRODUCT;

        } catch (NegativeItemQuantityException exception) {

            model.addAttribute(ERR_ATTR_NAME, NEGATIVE_QUANTITY_EXCEPTION_MSG);
            return EDIT_PRODUCT;

        } catch (ZeroLengthItemNameException exception) {

            model.addAttribute(ERR_ATTR_NAME, ZERO_LENGTH_NAME_EXCEPTION_MSG);
            return EDIT_PRODUCT;

        } catch (NullItemNameException exception) {

            model.addAttribute(ERR_ATTR_NAME, NULL_NAME_EXCEPTION_MSG);
            return EDIT_PRODUCT;

        } catch (NullItemIdException exception) {

            return REDIRECT_PRODUCT_LIST;

        } catch (RuntimeException exception) {

            model.addAttribute(ERR_ATTR_NAME, RUNTIME_EXCEPTION_MSG);
            return EDIT_PRODUCT;

        }

        return REDIRECT_PRODUCT_LIST;
    }

    @GetMapping({"/delete", "/delete/", "/delete/{productId}", "/delete/{productId}"})
    public String deleteProductPage(Model model, @PathVariable(required=false) String productId) {
        Product product;
        try {
            product = service.findById(productId);
        } catch (RuntimeException exception) {
            return REDIRECT_PRODUCT_LIST;
        }

        model.addAttribute(PRODUCT_ATTR_NAME, product);
        return DELETE_PRODUCT;
    }

    @PostMapping({"/delete", "/delete/", "/delete/{productId}", "/delete/{productId}"})
    public String deleteProductPost(Model model, @PathVariable(required=false) String productId, @ModelAttribute Product product, BindingResult result) {

        try {
            service.deleteById(productId);
        } catch (RuntimeException exception) {
            return REDIRECT_PRODUCT_LIST;
        }

        return REDIRECT_PRODUCT_LIST;
    }
}

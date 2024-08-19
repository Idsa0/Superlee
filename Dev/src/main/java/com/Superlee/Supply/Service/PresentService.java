package com.Superlee.Supply.Service;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.Superlee.Supply.Business.OrderFacade;
import com.Superlee.Supply.Business.SupplierFacade;
import com.Superlee.Supply.Business.Supplier.PaymentMethod;
import com.Superlee.Supply.Service.Responses.Response;

public class PresentService {

    private static PresentService instance;
    private final SupplierService ss;
    private final OrderService os;
    private SupplierFacade sf = new SupplierFacade();
    private OrderFacade of = new OrderFacade(sf);
    private static Scanner scanner = new Scanner(System.in);

    private PresentService() {
        ss = SupplierService.getInstance(sf);
        os = OrderService.getInstance(of);
    }

    public static PresentService getInstance() {
        if (instance == null)
            instance = new PresentService();
        return instance;
    }

    public Response call(String command) {
        String[] parts = command.split(" ");
        Response errResponse = new Response("Invalid number of args");
        switch (parts[0]) {
            case "28":
                return ss.loadData();
            case "29":
                return os.loadData();
            case "27":
                return orderCheapestOption();
            case "30":
                printMenu();
                return new Response();
            case "1":
                return addSupplier();
            case "2":
                return removeSupplier();
            case "3":
                return updateSupplierName();
            case "4":
                return updateSupplierBankAccount();
            case "18":
                return addGeneralOrder();
            case "19":
                return addRepOrder();
            case "20":
                return os.updateOrders();
            case "21":
                return removeOrder();
            case "22":
                return addProduct();
            case "23":
                return removeProduct();
            case "5":
                return addContact();
            case "6":
                return updateProductPrice();
            case "7":
                return updateSupplierPaymentMethod();
            case "8":
                return ss.getAllSuppliers();
            case "9":
                return ss.getAllContacts();
            case "10":
                return getSupplierAgreement();
            case "11":
                return getSupplier();
            case "24":
                return getOrder();
            case "25":
                return os.getAllOrders();
            case "26":
                return getOrderPrice();
            case "12":
                return addProductDiscountAccordingToAmount();
            case "13":
                return updateProductDiscountAccordingToAmount();
            case "14":
                return removeProductDiscountAccordingToAmount();
            case "15":
                return updateProductName();
            case "16":
                return addProductToSupplier();
            case "17":
                return removeProductFromSupplier();
            default:
                return new Response("Invalid command");
        }
    }
    public void printMenu() {
        System.out.println("""
                        Welcome to the Super-Lee System. Here are the available commands:

                        Supplier Commands:
                        1. addSupplier
                        2. removeSupplier
                        3. updateSupplierName
                        4. updateSupplierBankAccount
                        5. addContact
                        6. updateProductPrice
                        7. updateSupplierPaymentMethod
                        8. getAllSuppliers
                        9. getAllContacts
                        10. getSupplierAgreement
                        11. getSupplier
                        12. addProductDiscountAccordingToAmount
                        13. updateProductDiscountAccordingToAmount
                        14. removeProductDiscountAccordingToAmount
                        15. updateProductName
                        16. addProductToSupplier
                        17. removeProductFromSupplier

                        Order Commands:
                        18. addGeneralOrder
                        19. addRepOrder
                        20. updateOrders
                        21. removeOrder
                        22. addProduct
                        23. removeProduct
                        24. getOrder
                        25. getAllOrders
                        26. getOrderPrice
                        27. orderCheapestOption

                        General Commands:
                        28. loadSupplierData
                        29. loadOrderData
                        30. help
                        31. exit
                        """);
    }
    private Response addSupplier() {
        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();
        System.out.print("Enter company number: ");
        String companyNumber = scanner.nextLine();
        System.out.print("Enter bank number: ");
        String bankNumber = scanner.nextLine();
        System.out.print("Enter payment method (CASH/CHECK/BANK_TRANSFER/CREDIT_CARD): ");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(scanner.nextLine().toUpperCase());
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        return ss.addSupplier(name, companyNumber, bankNumber, paymentMethod, address);
    }

    private Response removeSupplier() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        return ss.removeSupplier(supplierId);
    }

    private Response updateSupplierName() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new supplier name: ");
        String newName = scanner.nextLine();
        return ss.updateSupplierName(supplierId, newName);
    }

    private Response updateSupplierBankAccount() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new bank account number: ");
        String newBankAccount = scanner.nextLine();
        return ss.updateSupplierBankAccount(supplierId, newBankAccount);
    }

    private Response addGeneralOrder() {
        System.out.print("Enter order details (catalogNumber1-amount1,catalogNumber2-amount2,...): ");
        String orderDetails = scanner.nextLine();
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        return os.addGeneralOrder(itemIdAndAmount(orderDetails), new Date(Calendar.getInstance().getTime().getTime()), supplierId);
    }

    private Response addRepOrder() {
        System.out.print("Enter order details (catalogNumber1-amount1,catalogNumber2-amount2,...): ");
        String orderDetails = scanner.nextLine();
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter day of the week (0-6 where 0 is Sunday): ");
        int day = Integer.parseInt(scanner.nextLine());
        return os.addRepOrder(itemIdAndAmount(orderDetails), new Date(Calendar.getInstance().getTime().getTime()), supplierId, day);
    }

    private Response removeOrder() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        return os.removeOrder(orderId);
    }

    private Response addProduct() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        int amount = Integer.parseInt(scanner.nextLine());
        return os.addProduct(orderId, catalogNumber, amount);
    }

    private Response removeProduct() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        return os.removeProduct(orderId, catalogNumber);
    }

    private Response addContact() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter contact name: ");
        String name = scanner.nextLine();
        System.out.print("Enter contact phone: ");
        String phone = scanner.nextLine();
        return ss.addContact(supplierId, name, phone);
    }

    private Response updateProductPrice() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new price: ");
        double newPrice = Double.parseDouble(scanner.nextLine());
        return ss.updateProductPrice(supplierId, catalogNumber, newPrice);
    }

    private Response updateSupplierPaymentMethod() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new payment method (CASH/BANK_TRANSFER/CHECK/CREDIT_CARD): ");
        PaymentMethod paymentMethod = PaymentMethod.valueOf(scanner.nextLine().toUpperCase());
        return ss.updateSupplierPaymentMethod(supplierId, paymentMethod);
    }

    private Response getSupplierAgreement() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        return ss.getSupplierAgreement(supplierId);
    }

    private Response getSupplier() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        return ss.getSupplier(supplierId);
    }

    private Response getOrder() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        return os.getOrder(orderId);
    }

    private Response getOrderPrice() {
        System.out.print("Enter order ID: ");
        int orderId = Integer.parseInt(scanner.nextLine());
        return os.getOrderPrice(orderId);
    }

    private Response addProductDiscountAccordingToAmount() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        int amount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter discount: ");
        int discount = Integer.parseInt(scanner.nextLine());
        return ss.addProductDiscountAccordingToAmount(supplierId, catalogNumber, amount, discount);
    }

    private Response updateProductDiscountAccordingToAmount() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        int amount = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new discount: ");
        int newDiscount = Integer.parseInt(scanner.nextLine());
        return ss.updateProductDiscountAccordingToAmount(supplierId, catalogNumber, amount, newDiscount);
    }

    private Response removeProductDiscountAccordingToAmount() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter amount: ");
        int amount = Integer.parseInt(scanner.nextLine());
        return ss.removeProductDiscountAccordingToAmount(supplierId, catalogNumber, amount);
    }

    private Response updateProductName() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new product name: ");
        String newName = scanner.nextLine();
        return ss.updateProductName(supplierId, catalogNumber, newName);
    }

    private Response addProductToSupplier() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        return ss.addProductToSupplier(supplierId, catalogNumber, price, name);
    }

    private Response removeProductFromSupplier() {
        System.out.print("Enter supplier ID: ");
        int supplierId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter catalog number: ");
        int catalogNumber = Integer.parseInt(scanner.nextLine());
        return ss.removeProductFromSupplier(supplierId, catalogNumber);
    }

    // A helper method to parse the item id and amount from the command
    private Map<Integer, Integer> itemIdAndAmount(String string) {
        Map<Integer, Integer> itemIdAndAmount = new HashMap<>();
        String[] orderDetails = string.split(",");
        for (String detail : orderDetails) {
            String[] item = detail.split("-");
            int itemId = Integer.parseInt(item[0]);
            int amount = Integer.parseInt(item[1]);
            itemIdAndAmount.put(itemId, amount);
        }
        return itemIdAndAmount;
    }

    private Response orderCheapestOption(){
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter amount: ");
        int amount = Integer.parseInt(scanner.nextLine());
        return os.orderCheapestOption(name, amount, new Date(Calendar.getInstance().getTime().getTime()));
    }
}

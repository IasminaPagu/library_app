# LibriSfera

LibriSfera is a full-stack web application designed for managing a digital library experience, enabling users to browse books, manage their cart and wishlist, and place orders. It also includes a dedicated administration section for managing the inventory via CRUD operations.

### **Features**

🏠**Home Navigation**
-clicking on the logo redirects the user to the home page for seamless navigation.  


🛒 **Cart Functionality**
-users can add books to the cart using the "Adaugă în coș" button.  
-a book can only be added to the cart if its stock > 0.  
-in the cart view, users can click "Finalizare comandă" to complete their order.  
-orders can only be finalized if the book is still available in stock (stock > 0).  


📑**Wishlist**
-users can add books to a wishlist for future interest using the "Adaugă în wishlist" button.  


✨**Theme Toggle**
-users can switch between Light Mode and Dark Mode via the "Dark Mode" toggle button.  


📄**CSV Export**
-Admins can download a CSV containing customer data for reporting and analysis purposes.


🔧**Admin Dashboard (CRUD)**

If logged in with admin credentials, users gain access to a management panel to:
-add new books  
-edit existing book information  
-delete books from the catalog  
-update stock values  

### **User Roles**

**Regular Users:**
-browse books  
-add books to cart or wishlist  
-place orders if stock is available  

**Admin Users:**
-all regular features  
-access to inventory management features (CRUD)  
-view and export customer data  



### **Technologies Used**

Frontend: **Angular** (TypeScript, HTML, CSS)  
Backend: **Spring Boot** (Java)  
Database: **MySQL**  
Authentication: **JWT-based login system** and **Axios Service**  


### **Setup Instructions**
Backend  

cd backend  
mvn clean install  
./mvnw spring-boot:run  

Frontend  
cd frontend  
npm install  
npm start  



### **Notes**

All endpoints are protected by JWT-based authentication.  
Admin features are automatically enabled for users with the ADMIN role.  
Order finalization and cart addition are strictly stock-dependent.  
Messages are displayed in Romanian for user clarity.  
This project is licensed for educational and demonstration purposes.  


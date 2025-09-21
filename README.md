# Step 1 — Product image upload (Spring Boot)

**What you get**
- REST API to create products and upload product images.
- Stores images to local `uploads/` folder.
- Serves images publicly at `GET /files/{filename}` as absolute URLs in product records.
- H2 in-memory DB for instant startup.

## Run
```bash
/backend/mvn spring-boot:run   # or: mvn spring-boot:run
```
Open `frontends/admin-basic.html` in your browser and use the forms.

## API
- `POST /api/products` — JSON `{ name, description, price }`
- `POST /api/products/{id}/image` — `multipart/form-data` with field `file`
- `GET /api/products` — list products

## Next steps
- Switch DB to PostgreSQL (add driver + connection + docker-compose).
- Add auth (admin login).
- Add React Admin UI (or hook into your existing site).
- Add Cloud storage (S3/Cloudinary) and a CDN for production.
 

# structure

product-image-upload-step1/
├─ pom.xml
├─ README.md
├─ src/
│  ├─ main/
│  │  ├─ java/com/example/catalog/
│  │  │  ├─ CatalogApplication.java
│  │  │  ├─ domain/
│  │  │  │   └─ Product.java
│  │  │  ├─ repo/
│  │  │  │   └─ ProductRepository.java
│  │  │  ├─ api/
│  │  │  │   ├─ ProductController.java
│  │  │  │   └─ dto/
│  │  │  │       ├─ CreateProductRequest.java
│  │  │  │       └─ ProductResponse.java
│  │  │  ├─ service/
│  │  │  │   └─ FileStorageService.java
│  │  │  └─ config/
│  │  │      └─ WebConfig.java
│  │  └─ resources/
│  │      └─ application.yml
└─ frontends/
   └─ admin-basic.html

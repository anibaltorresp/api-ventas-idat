package pe.edu.idat.api_ventas_idat.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.idat.api_ventas_idat.model.Product;
import pe.edu.idat.api_ventas_idat.repository.projection.ProductStockProjection;

import java.util.List;

@Repository
public interface ProductRepository
        extends JpaRepository<Product, Integer> {

    //select * from products where discontinued = ?;
    List<Product> findByDiscontinued(Boolean discontinued);

    //Sintaxis JPQL
    @Query("SELECT p FROM Product p WHERE p.discontinued=false")
    List<Product> obtenerProductosActivos();

    @Query(value = "SELECT * FROM products WHERE discontinued=0",
            nativeQuery = true)
    List<Product> obtenerProductosActivosSQL();



@Query (value = "select p.ProductID, p.ProductName, p.UnitPrice, " +
        "c.CategoryName, s.CompanyName, p.UnitsInStock from products p " +
        "inner join categories c on p.CategoryID = c.CategoryID " +
        "inner join suppliers s on p.SupplierID = s.SupplierID " +
        "where p.UnitsInStock <= :unitsInStock;",
nativeQuery = true)
    List<ProductStockProjection> obtenerTodosLosProductos(@Param("unitsInStock") Integer unitsInStock);

@Modifying
@Transactional
@Query(value = "CALL RegistrarProducto(:productname, :supplierid, :categoryid, :unitprice)",
    nativeQuery = true)
    void registrarProducto(
            @Param("productname") String productname,
            @Param("supplierid") Integer supplierid,
            @Param("categoryid") Integer categoryid,
            @Param("unitprice") Double unitprice);

    @Modifying
    @Transactional
    @Query(value = "CALL ActualizarProducto(:productid, :productname, :supplierid, :categoryid)",
            nativeQuery = true)
    void actualizarProducto(
            @Param("productid") Integer productid,
            @Param("productname") String productname,
            @Param("supplierid") Integer supplierid,
            @Param("categoryid") Integer categoryid);

}

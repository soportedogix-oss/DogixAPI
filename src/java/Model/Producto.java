package Model;

public class Producto {

    private int id;
    private String nombre;
    private Double precio; // 🔥 mejor que double
    private String descripcion;
    private String imagen;
    private int stock;

    public Producto() {}

    public Producto(int id, String nombre, Double precio, String descripcion, String imagen, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.stock = stock;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    // 🔥 DEBUG
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", stock=" + stock +
                '}';
    }

    // 🔥 VALIDACIÓN OPCIONAL
    public boolean esValido() {
        return nombre != null && !nombre.isEmpty() && precio != null && precio > 0;
    }
}
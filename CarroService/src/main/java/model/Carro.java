package model;

import java.io.Serializable;

public class Carro implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String MODELO_PADRAO = "Modelo Desconhecido";
    public static final String MARCA_PADRAO = "Marca Desconhecida";
    public static final String TIPO_PADRAO = "hatch";
    
    private int id;
    private String modelo;
    private String marca;
    private String tipo;
    
    public Carro() {
        id = -1;
        modelo = MODELO_PADRAO;
        marca = MARCA_PADRAO;
        tipo = TIPO_PADRAO;
    }
    
    public Carro(int id, String modelo, String marca, String tipo) {
        setId(id);
        setModelo(modelo);
        setMarca(marca);
        setTipo(tipo);
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        if (id >= 0)
            this.id = id;
    }
    
    public String getModelo() {
        return modelo;
    }
    
    public void setModelo(String modelo) {
        if (modelo != null && modelo.length() >= 2)
            this.modelo = modelo;
    }
    
    public String getMarca() {
        return marca;
    }
    
    public void setMarca(String marca) {
        if (marca != null && marca.length() >= 2)
            this.marca = marca;
    }
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        if (tipo != null && !tipo.trim().isEmpty())
            this.tipo = tipo.toLowerCase();
        else
            this.tipo = TIPO_PADRAO;
    }
    
    @Override
    public String toString() {
        return "Carro [ID: " + id + 
               " | Modelo: " + modelo + 
               " | Marca: " + marca + 
               " | Tipo: " + tipo + "]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Carro carro = (Carro) obj;
        return this.getId() == carro.getId();
    }
}

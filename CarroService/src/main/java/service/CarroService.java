package service;

import java.sql.SQLException;

import dao.CarroDAO;
import model.Carro;
import spark.Request;
import spark.Response;

public class CarroService {

    private CarroDAO carroDAO;

    public CarroService() {
        try {
            carroDAO = new CarroDAO();
        } catch (SQLException e) {
            System.out.println("Erro ao conectar com o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object add(Request request, Response response) {
        String modelo = request.queryParams("modelo");
        String marca = request.queryParams("marca");
        String tipo = request.queryParams("tipo");

        if (modelo == null || marca == null || tipo == null ||
            modelo.trim().isEmpty() || marca.trim().isEmpty() || tipo.trim().isEmpty()) {
            response.status(400);
            return "Erro: Todos os campos (modelo, marca, tipo) são obrigatórios.";
        }

        Carro carro = new Carro();
        carro.setModelo(modelo);
        carro.setMarca(marca);
        carro.setTipo(tipo);

        carroDAO.add(carro);

        response.status(201);
        return carro.getId();
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
        Carro carro = carroDAO.get(id);
        
        if (carro != null) {
            response.header("Content-Type", "application/xml");
            response.header("Content-Encoding", "UTF-8");

            return "<carro>\n" + 
                    "\t<id>" + carro.getId() + "</id>\n" +
                    "\t<modelo>" + carro.getModelo() + "</modelo>\n" +
                    "\t<marca>" + carro.getMarca() + "</marca>\n" +
                    "\t<tipo>" + carro.getTipo() + "</tipo>\n" +
                    "</carro>\n";
        } else {
            response.status(404);
            return "Carro " + id + " não encontrado.";
        }
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
        Carro carro = carroDAO.get(id);

        if (carro != null) {
            String modelo = request.queryParams("modelo");
            String marca = request.queryParams("marca");
            String tipo = request.queryParams("tipo");
            
            if (modelo != null && !modelo.trim().isEmpty()) {
                carro.setModelo(modelo);
            }
            if (marca != null && !marca.trim().isEmpty()) {
                carro.setMarca(marca);
            }
            if (tipo != null && !tipo.trim().isEmpty()) {
                carro.setTipo(tipo);
            }

            carroDAO.update(carro);
            
            response.status(200);
            return id;
        } else {
            response.status(404);
            return "Carro não encontrado.";
        }
    }

    public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));

        Carro carro = carroDAO.get(id);

        if (carro != null) {
            carroDAO.remove(carro);
            response.status(200);
            return id;
        } else {
            response.status(404);
            return "Carro não encontrado.";
        }
    }

    public Object getAll(Request request, Response response) {
        StringBuffer returnValue = new StringBuffer("<carros type=\"array\">");
        for (Carro carro : carroDAO.getAll()) {
            returnValue.append("\n<carro>\n" + 
                    "\t<id>" + carro.getId() + "</id>\n" +
                    "\t<modelo>" + carro.getModelo() + "</modelo>\n" +
                    "\t<marca>" + carro.getMarca() + "</marca>\n" +
                    "\t<tipo>" + carro.getTipo() + "</tipo>\n" +
                    "</carro>\n");
        }
        returnValue.append("</carros>");
        response.header("Content-Type", "application/xml");
        response.header("Content-Encoding", "UTF-8");
        return returnValue.toString();
    }
}

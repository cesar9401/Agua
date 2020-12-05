<%-- 
    Document   : crear-socio
    Created on : 4 dic. 2020, 22:56:15
    Author     : julio
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<!DOCTYPE html>
<html>
    <head>
        <%@include file="resources/css/css.html" %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style>
            input[type=number]::-webkit-inner-spin-button, 
            input[type=number]::-webkit-outer-spin-button { 
                -webkit-appearance: none; 
                margin: 0; 
            }

            input[type=number] { -moz-appearance:textfield; }
        </style>
        <title>Iniciar Sesion</title>
    </head>





    <%@include file="html/crear-socio-manco.html" %>

    <%@include file="resources/js/js.html" %>
    
    <script src="resources/js/dataTables.jqueryui.min.js"></script>
    <script src="resources/js/jquery.dataTables.min.js"></script>
    <script>
        
        $(document).ready(function () {
            $('#myTable').DataTable({
                language: {
                    url: 'Spanish.json'
                }
            });
        });


    </script>
</body>
</html>

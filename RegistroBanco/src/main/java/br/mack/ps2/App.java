package br.mack.ps2;
import java.math.BigDecimal;
import java.util.*;
import java.sql.*;
public class App {
    public static void main( String[] args ) {
        Connection conn = null;
        Scanner in = new Scanner(System.in);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://192.168.99.100:32771/Mackenzie";
            String user = "root";
            String psw = "root";

            conn = DriverManager.getConnection(url, user, psw);

            String createSQL = "INSERT INTO contas VALUES(?,?)";
            String readSQL = "SELECT * FROM contas";
            //não tem updateSQL
            String deleteSQL = "DELETE FROM contas WHERE nro_conta = ?";

            int op;
            long nroConta;
            BigDecimal saldo;
            do {
                System.out.println("====MENU====");
                System.out.println("\n1.Inserir\n2.Deletar\n3.Ver contas\n4.Sair");
                System.out.print("Opção: ");
                op = in.nextInt();
                switch(op){
                    case 1:
                        try{
                            PreparedStatement stm = conn.prepareStatement(createSQL);
                            System.out.print("Número da conta: ");
                            nroConta = in.nextLong();
                            System.out.print("Saldo: ");
                            saldo = in.nextBigDecimal();
                            stm.setLong(1,nroConta);
                            stm.setBigDecimal(2,saldo);
                            stm.execute();

                            System.out.println("Dados inseridos com sucesso!");
                        } catch (final SQLException err){
                            System.out.println("Falha na conexão com o Banco de Dados");
                        } finally {
                            try{
                                conn.close();
                            } catch (final Exception err){
                                err.printStackTrace();
                            }
                        }
                        break;
                    case 2:
                        try{
                            PreparedStatement stm = conn.prepareStatement(deleteSQL);
                            System.out.print("Número da conta: ");
                            nroConta = in.nextLong();
                            stm.setLong(1, nroConta);
                            stm.execute();

                            System.out.println("Conta deletada com sucesso!");
                        } catch (final SQLException err){
                            System.out.println("Falha na conexão com o Banco de Dados!");
                        } finally {
                            try{
                                conn.close();
                            } catch (final Exception err){
                                err.printStackTrace();
                            }
                        }
                        break;
                    case 3:
                        PreparedStatement stm = conn.prepareStatement(readSQL);
                        ResultSet rs = stm.executeQuery();
                        int cont = 0;
                        while(rs.next()){
                            cont++;
                            System.out.println(cont + "º Registro:");
                            nroConta = rs.getLong("nro_conta");
                            saldo = rs.getBigDecimal("saldo");

                            System.out.println("Conta: " + nroConta + " - Saldo: " + saldo);

                        }
                        rs.close();
                        break;
                    default:
                        break;
                }
            } while (op != 4);
        } catch (final ClassNotFoundException err) {
            System.out.println("Falha no carregamento do Driver JDBC");
            err.printStackTrace();
        } catch (final SQLException err) {
            System.out.println("Falha de conexão com o Banco de Dados");
            err.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (final Exception err) {
                err.printStackTrace();
            }
        }
    }
}

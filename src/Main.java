import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        DataSource dataSource = createDataSource();
        try (Connection connection = dataSource.getConnection()) {
            System.out.println("Uspješno spajanje na bazu.");
            // Isključujemo automatski commit
            connection.setAutoCommit(false);
            try {
                // Stavka 8 - povećanje cijene za 10
                String sql1 = "UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu + 10 WHERE IDStavka = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql1)) {
                    ps.setInt(1, 8);
                    int brojRedaka = ps.executeUpdate();
                    if (brojRedaka == 0) {
                        throw new SQLException("Stavka s ID-em 8 ne postoji.");
                    }
                    System.out.println("Cijena stavke 8 povećana za 10.");
                }

                // Stavka 9 - smanjenje cijene za 10
                String sql2 = "UPDATE Stavka SET CijenaPoKomadu = CijenaPoKomadu - 10 WHERE IDStavka = ?";
                try (PreparedStatement ps = connection.prepareStatement(sql2)) {
                    ps.setInt(1, 9);
                    int brojRedaka = ps.executeUpdate();
                    if (brojRedaka == 0) {
                        throw new SQLException("Stavka s ID-em 9 ne postoji.");
                    }
                    System.out.println("Cijena stavke 9 smanjena za 10.");
                }

                // Ako su obje naredbe uspješne
                connection.commit();
                System.out.println("Transakcija uspješno završena.");
            } catch (SQLException e) {
                // Ako bilo koja naredba nije uspjela
                connection.rollback();
                System.err.println("Greška! Transakcija je poništena.");
                System.err.println("Nijedna promjena nije spremljena u bazu.");
                e.printStackTrace();
            }

        } catch (SQLException e) {
            System.err.println("Greška prilikom spajanja na bazu podataka!");
            e.printStackTrace();
        }
    }

    private static DataSource createDataSource()
    {
        SQLServerDataSource ds = new SQLServerDataSource();
        ds.setServerName("localhost");
        ds.setDatabaseName("AdventureWorksOBP");
        ds.setUser("sa");
        ds.setPassword("SQL");
        ds.setEncrypt("false");
        return ds;
    }

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package kesehatan;
    import java.sql.Driver;
    import java.sql.DriverManager;
    import java.sql.Connection;
    import java.sql.SQLException;
    import java.sql.Statement;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import javax.swing.JOptionPane;


/**
 *
 * @author AhmadRiyo
 */
public class dbCRUD {
    
    String jdbcURL ="jdbc:mysql://localhost:3306/ahmadriyokusuma_2210010085_pbo2";
    String username ="root";
    String password ="";
    
    Connection koneksi;
    
    public dbCRUD(){
        try (Connection Koneksi = DriverManager.getConnection(jdbcURL, username, password)){
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    
    public dbCRUD(String url, String user, String pass){
        
        try(Connection Koneksi = DriverManager.getConnection(url, user, pass)) {
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            System.out.println("Berhasil");
        } catch (Exception error) {
            System.err.println(error.toString());
        }
        
    }
    
    
    public static Connection getKoneksi() {
        try {
            String url = "jdbc:mysql://localhost:3306/ahmadriyokusuma_2210010085_pbo2";
            String username = "root"; 
            String password = "";
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
            return null;
        }
    }

    
    public static boolean duplicateKey(String table, String PrimaryKey, String value){
        boolean hasil=false;
        
        try {
            Statement sts = getKoneksi().createStatement();
            ResultSet rs = sts.executeQuery("SELECT*FROM "+table+" WHERE "+PrimaryKey+" ='"+value+"'");
            hasil = rs.next();
            
            rs.close();
            sts.close();
            getKoneksi().close();
            
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
        
        return hasil;
    }
    
    public static void TambahDinamis(String NamaTabel, String[] Field, String[] Value) {
    try {
        StringBuilder field = new StringBuilder();
        StringBuilder value = new StringBuilder();

        for (int i = 0; i < Field.length; i++) {
            field.append(Field[i]);
            value.append("'").append(Value[i]).append("'");

            if (i < Field.length - 1) { // Tambahkan koma jika bukan elemen terakhir
                field.append(", ");
                value.append(", ");
            }
        }

        String SQLTambah = "INSERT INTO " + NamaTabel + " (" + field.toString() + ") VALUES (" + value.toString() + ")";

        Statement perintah = getKoneksi().createStatement();
        perintah.executeUpdate(SQLTambah);

        JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");

        // Tutup Statement dan koneksi
        perintah.close();
        getKoneksi().close();

    } catch (Exception e) {
        System.err.println(e.toString());
    }
}
    
    public String getTabelField(String[] Field) {
    // Menggabungkan field dengan koma
    return "(" + String.join(", ", Field) + ")";
    }

    public String getTabelValue(String[] Value) {
        // Menggabungkan value dengan koma dan menambahkan tanda kutip untuk nilai string
        return "(" + String.join(", ", Value) + ")";
    }

    
// Method untuk membangun nilai field yang akan di-update
     public static String getFieldValueEdit(String[] Field, String[] value){
        String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }

     
    // Method untuk update data secara dinamis
    public static void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary, String[] Field, String[] Value) {
        try {
            // Mencari apakah ID yang diberikan ada di dalam tabel
            String SQLCheck = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + PrimaryKey + " = '" + IsiPrimary + "'";
            Statement perintah = getKoneksi().createStatement();
            ResultSet rs = perintah.executeQuery(SQLCheck);

            if (rs.next() && rs.getInt(1) > 0) { // Jika ID ditemukan
                // Melakukan update jika ID ada
                String SQLUbah = "UPDATE " + NamaTabel + " SET " + getFieldValueEdit(Field, Value) + " WHERE " + PrimaryKey + "='" + IsiPrimary + "'";
                perintah.executeUpdate(SQLUbah);
                JOptionPane.showMessageDialog(null, "Data Berhasil DiUpdate");
            } else { // Jika ID tidak ditemukan
                JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
            }

            rs.close(); // Tutup ResultSet
            perintah.close(); // Tutup Statement
            getKoneksi().close(); // Tutup koneksi

        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }


     
    public static void HapusDinamis(String NamaTabel, String PK, String isi) {
        try {
            // Mencari apakah ID yang diberikan ada di dalam tabel
            String SQLCheck = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + PK + " = '" + isi + "'";
            Statement perintah = getKoneksi().createStatement();
            ResultSet rs = perintah.executeQuery(SQLCheck);

            if (rs.next() && rs.getInt(1) > 0) { // Jika ID ditemukan
                // Menghapus data jika ID ditemukan
                String SQLDelete = "DELETE FROM " + NamaTabel + " WHERE " + PK + "='" + isi + "'";
                perintah.executeUpdate(SQLDelete);
                JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");
            } else { // Jika ID tidak ditemukan
                JOptionPane.showMessageDialog(null, "Data Tidak Ditemukan");
            }

            rs.close(); // Tutup ResultSet
            perintah.close(); // Tutup Statement
            getKoneksi().close(); // Tutup koneksi

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}

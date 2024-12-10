/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package configdatabase;
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
public class configDB {
        String jdbcURL ="jdbc:mysql://localhost:3306/ahmadriyokusuma_2210010085_pbo2";
    String username ="root";
    String password ="";
    
    Connection koneksi;
    
    public configDB(){
        try (Connection Koneksi = DriverManager.getConnection(jdbcURL, username, password)){
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    
        public configDB(String url, String user, String pass){
        
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
            String url = "jdbc:mysql://localhost:3306/ahmadriyokusuma_2210010085_pbo2";  // Ganti dengan URL dan database Anda
            String username = "root";  // Ganti dengan username database Anda
            String password = "";  // Ganti dengan password database Anda
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
    
    public static void TambahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary, String[] Field, String[] Value) {
    try {
        // Mengecek apakah data dengan primary key tersebut sudah ada di dalam tabel
        String SQLCheck = "SELECT COUNT(*) FROM " + NamaTabel + " WHERE " + PrimaryKey + " = '" + IsiPrimary + "'";
        Statement perintah = getKoneksi().createStatement();
        ResultSet rs = perintah.executeQuery(SQLCheck);

        if (rs.next() && rs.getInt(1) > 0) { // Jika ID ditemukan
            JOptionPane.showMessageDialog(null, "Data dengan ID tersebut sudah ada.");
        } else { // Jika ID tidak ditemukan, lakukan operasi INSERT
            // Membentuk query INSERT INTO
            StringBuilder fieldsBuilder = new StringBuilder();
            StringBuilder valuesBuilder = new StringBuilder();

            for (int i = 0; i < Field.length; i++) {
                fieldsBuilder.append(Field[i]);
                valuesBuilder.append("'").append(Value[i]).append("'");

                if (i < Field.length - 1) { // Tambahkan koma jika bukan elemen terakhir
                    fieldsBuilder.append(", ");
                    valuesBuilder.append(", ");
                }
            }

            String SQLTambah = "INSERT INTO " + NamaTabel + " (" + PrimaryKey + ", " + fieldsBuilder.toString() + ") VALUES ('" + IsiPrimary + "', " + valuesBuilder.toString() + ")";
            perintah.executeUpdate(SQLTambah);
            JOptionPane.showMessageDialog(null, "Data Berhasil Ditambahkan");
        }

        rs.close(); // Menutup ResultSet
        perintah.close(); // Menutup Statement
        getKoneksi().close(); // Menutup koneksi

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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu extends JFrame {
    public static void main(String[] args) {
        // buat object window
        Menu window = new Menu();

        // atur ukuran window
        window.setSize(480, 560);
        // letakkan window di tengah layar
        window.setLocationRelativeTo(null);
        // isi window
        window.setContentPane(window.mainPanel);
        // ubah warna background
        window.getContentPane().setBackground(Color.white);
        // tampilkan window
        window.setVisible(true);
        // agar program ikut berhenti saat window diclose
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // index baris yang diklik
    private int selectedIndex = -1;
    // list untuk menampung semua mahasiswa
    private ArrayList<Mahasiswa> listMahasiswa;
    private Database database;

    private String getSelectedStatus() {
        if (aktifRadio.isSelected()) {
            return "Aktif";
        } else if (cutiRadio.isSelected()) {
            return "Cuti";
        } else if (dropOutRadio.isSelected()) {
            return "Dropout";
        }
        return "Aktif";
    }

    private void setSelectedStatus(String status) {
        switch (status) {
            case "Aktif":
                aktifRadio.setSelected(true);
                break;
            case "Cuti":
                cutiRadio.setSelected(true);
                break;
            case "Dropout":
                dropOutRadio.setSelected(true);
                break;
            default:
                aktifRadio.setSelected(true);
                break;
        }
    }

    private JPanel mainPanel;
    private JTextField nimField;
    private JTextField namaField;
    private JTable mahasiswaTable;
    private JButton addUpdateButton;
    private JButton cancelButton;
    private JComboBox jenisKelaminComboBox;
    private JButton deleteButton;
    private JLabel titleLabel;
    private JLabel nimLabel;
    private JLabel namaLabel;
    private JLabel jenisKelaminLabel;
    private JLabel statusLabel;
    private JRadioButton aktifRadio;
    private JRadioButton cutiRadio;
    private JRadioButton dropOutRadio;
    private ButtonGroup statusButtonGroup;

    // constructor
    public Menu() {
        // inisialisasi listMahasiswa
        listMahasiswa = new ArrayList<>();

        database = new Database();

        // Inisialisasi komponen-komponen UI jika belum dibuat oleh GUI Builder
        if (mainPanel == null) {
            initializeComponents();
        }

        // isi tabel mahasiswa
        mahasiswaTable.setModel(setTable());

        // ubah styling title
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 20f));

        // atur isi combo box
        String[] jenisKelaminData = {"", "Laki-laki", "Perempuan"};
        jenisKelaminComboBox.setModel(new DefaultComboBoxModel(jenisKelaminData));

        // atur radio button status
        statusButtonGroup = new ButtonGroup();
        statusButtonGroup.add(aktifRadio);
        statusButtonGroup.add(cutiRadio);
        statusButtonGroup.add(dropOutRadio);

        // set default selection
        aktifRadio.setSelected(true);

        // sembunyikan button delete
        deleteButton.setVisible(false);

        // saat tombol add/update ditekan
        addUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex == -1) {
                    insertData();
                } else {
                    updateData();
                }
            }
        });
        // saat tombol delete ditekan
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedIndex >= 0) {
                    deleteData();
                }
            }
        });
        // saat tombol cancel ditekan
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });
        // saat salah satu baris tabel ditekan
        mahasiswaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // ubah selectedIndex menjadi baris tabel yang diklik
                selectedIndex = mahasiswaTable.getSelectedRow();

                // simpan value textfield dan combo box
                String selectedNim = mahasiswaTable.getModel().getValueAt(selectedIndex, 1).toString();
                String selectedNama = mahasiswaTable.getModel().getValueAt(selectedIndex, 2).toString();
                String selectedJenisKelamin = mahasiswaTable.getModel().getValueAt(selectedIndex, 3).toString();
                String selectedStatus = mahasiswaTable.getModel().getValueAt(selectedIndex, 4).toString();

                // ubah isi textfield dan combo box
                nimField.setText(selectedNim);
                namaField.setText(selectedNama);
                jenisKelaminComboBox.setSelectedItem(selectedJenisKelamin);

                // set radio button status
                setSelectedStatus(selectedStatus);

                // ubah button "Add" menjadi "Update"
                addUpdateButton.setText("Update");
                // tampilkan button delete
                deleteButton.setVisible(true);
            }
        });
    }

    // Metode untuk inisialisasi komponen jika diperlukan
    private void initializeComponents() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        // Inisialisasi semua komponen
        titleLabel = new JLabel("Data Mahasiswa");
        nimLabel = new JLabel("NIM");
        namaLabel = new JLabel("Nama");
        jenisKelaminLabel = new JLabel("Jenis Kelamin");
        statusLabel = new JLabel("Status");

        nimField = new JTextField(20);
        namaField = new JTextField(20);
        jenisKelaminComboBox = new JComboBox();

        aktifRadio = new JRadioButton("Aktif");
        cutiRadio = new JRadioButton("Cuti");
        dropOutRadio = new JRadioButton("Dropout");

        addUpdateButton = new JButton("Add");
        cancelButton = new JButton("Cancel");
        deleteButton = new JButton("Delete");

        mahasiswaTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(mahasiswaTable);

        // Mengatur layout komponen
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        mainPanel.add(titleLabel, gbc);

        // NIM
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(nimLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        mainPanel.add(nimField, gbc);

        // Nama
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(namaLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(namaField, gbc);

        // Jenis Kelamin
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        mainPanel.add(jenisKelaminLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        mainPanel.add(jenisKelaminComboBox, gbc);

        // Status
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        mainPanel.add(statusLabel, gbc);

        // Panel untuk radio buttons
        JPanel radioPanel = new JPanel();
        radioPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(aktifRadio);
        radioPanel.add(cutiRadio);
        radioPanel.add(dropOutRadio);

        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        mainPanel.add(radioPanel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(addUpdateButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(deleteButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        mainPanel.add(buttonPanel, gbc);

        // Table
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        mainPanel.add(scrollPane, gbc);
    }

    public final DefaultTableModel setTable() {
        // tentukan kolom tabel
        Object[] column = {"No", "NIM", "Nama", "Jenis Kelamin", "Status"};

        // buat objek tabel dengan kolom yang sudah dibuat
        DefaultTableModel temp = new DefaultTableModel(null, column);

        try
        {
            ResultSet resultSet = database.selectQuery("SELECT * FROM mahasiswa");
            int i = 0;
            while(resultSet.next())
            {
                Object[] row = new Object[5];
                row[0] = i + 1;
                row[1] = resultSet.getString("nim");
                row[2] = resultSet.getString("nama");
                row[3] = resultSet.getString("jenis_kelamin");
                row[4] = resultSet.getString("status");

                temp.addRow(row);
                i++;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return temp;
        // isi tabel dengan listMahasiswa
    }

    public void insertData() {
        // ambil value dari textfield, combobox, dan radio button
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String status = getSelectedStatus();

        // validasi input kosong
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "NIM, Nama, dan Jenis Kelamin harus diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // mengecek nim yang sudah ada
        try {
            String query = "SELECT * FROM mahasiswa WHERE nim = '" + nim + "'";
            ResultSet rs = database.executeQuery(query);

            if (rs.next()) {
                JOptionPane.showMessageDialog(null,
                        "NIM sudah terdaftar. Silakan gunakan NIM lain.",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String sql = "INSERT INTO mahasiswa VALUES (null, '" + nim + "', '" + nama + "', '" + jenisKelamin + "', '" + status + "');";
        database.insertUpdateDeleteQuery(sql);

        // update tabel
        mahasiswaTable.setModel(setTable());

        // bersihkan form
        clearForm();

        // feedback
        System.out.println("Insert berhasil!");
        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan");
    }

    public void updateData() {
        // ambil data dari form
        String nim = nimField.getText();
        String nama = namaField.getText();
        String jenisKelamin = jenisKelaminComboBox.getSelectedItem().toString();
        String status = getSelectedStatus();

        // validasi input kosong
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "NIM, Nama, dan Jenis Kelamin harus diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // validasi input
        if (nim.isEmpty() || nama.isEmpty() || jenisKelamin.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                    "NIM, Nama, dan Jenis Kelamin harus diisi!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // konfirmasi update
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin mengupdate data?",
                "Konfirmasi Update",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Perbaikan: Gunakan query update berdasarkan NIM yang dipilih
                String sql = "UPDATE mahasiswa SET " +
                        "nim = '" + nim + "', " +
                        "nama = '" + nama + "', " +
                        "jenis_kelamin = '" + jenisKelamin + "', " +
                        "status = '" + status + "' " +
                        "WHERE nim = '" + nim + "'";

                // eksekusi query update
                database.insertUpdateDeleteQuery(sql);

                // Perbaikan: Refresh tabel setelah update
                mahasiswaTable.setModel(setTable());

                // bersihkan form
                clearForm();

                // tampilkan pesan sukses
                JOptionPane.showMessageDialog(null,
                        "Data berhasil diupdate!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                // tangani error
                JOptionPane.showMessageDialog(null,
                        "Gagal mengupdate data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void deleteData() {
        // ambil NIM dari baris yang dipilih
        String nimMahasiswa = nimField.getText();

        // konfirmasi hapus data
        int confirm = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin menghapus data dengan NIM: " + nimMahasiswa + "?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // query SQL untuk delete berdasarkan NIM
                String sql = "DELETE FROM mahasiswa WHERE nim = '" + nimMahasiswa + "'";

                // eksekusi query delete
                database.insertUpdateDeleteQuery(sql);

                // refresh tabel
                mahasiswaTable.setModel(setTable());

                // bersihkan form
                clearForm();

                // tampilkan pesan sukses
                JOptionPane.showMessageDialog(null,
                        "Data berhasil dihapus!",
                        "Informasi",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                // tangani error
                JOptionPane.showMessageDialog(null,
                        "Gagal menghapus data: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    public void clearForm() {
        // kosongkan semua texfield dan combo box
        nimField.setText("");
        namaField.setText("");
        jenisKelaminComboBox.setSelectedItem("");

        // Reset radio button ke default (Aktif)
        aktifRadio.setSelected(true);

        // ubah button "Update" menjadi "Add"
        addUpdateButton.setText("Add");
        // sembunyikan button delete
        deleteButton.setVisible(false);
        // ubah selectedIndex menjadi -1 (tidak ada baris yang dipilih)
        selectedIndex = -1;
    }

    private void populateList() {
        listMahasiswa.add(new Mahasiswa("2203999", "Amelia Zalfa Julianti", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202292", "Muhammad Iqbal Fadhilah", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202346", "Muhammad Rifky Afandi", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2210239", "Muhammad Hanif Abdillah", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2202046", "Nurainun", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2205101", "Kelvin Julian Putra", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2200163", "Rifanny Lysara Annastasya", "Perempuan", "Dropout"));
        listMahasiswa.add(new Mahasiswa("2202869", "Revana Faliha Salma", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2209489", "Rakha Dhifiargo Hariadi", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2203142", "Roshan Syalwan Nurilham", "Laki-laki", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2200311", "Raden Rahman Ismail", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2200978", "Ratu Syahirah Khairunnisa", "Perempuan", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2204509", "Muhammad Fahreza Fauzan", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2205027", "Muhammad Rizki Revandi", "Laki-laki", "Dropout"));
        listMahasiswa.add(new Mahasiswa("2203484", "Arya Aydin Margono", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2200481", "Marvel Ravindra Dioputra", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2209889", "Muhammad Fadlul Hafiizh", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2206697", "Rifa Sania", "Perempuan", "Cuti"));
        listMahasiswa.add(new Mahasiswa("2207260", "Imam Chalish Rafidhul Haque", "Laki-laki", "Aktif"));
        listMahasiswa.add(new Mahasiswa("2204343", "Meiva Labibah Putri", "Perempuan", "Aktif"));
    }
}
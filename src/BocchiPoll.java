import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BocchiPoll extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:bocchi_poll.db";
    private ButtonGroup waifuGroup;
    private JPanel resultsContainer; 
    
    // --- PALET WARNA DARK MODE ---
    private final Color bgDark = new Color(30, 30, 30);       
    private final Color panelDark = new Color(43, 43, 43);    
    private final Color textLight = new Color(235, 235, 235); 
    private final Color borderDark = new Color(70, 70, 70);   

    private final String[] waifus = {
        "Hitori Gotoh", "Nijika Ijichi", "Ryo Yamada", "Kita Ikuyo", 
        "Seika Ijichi", "PA-san", "Kikuri Hiroi", "Yoyoko Ohtsuki", 
        "Michiyo Gotoh", "Eliza Shimizu"
    };

    public BocchiPoll() {
        setupDatabase();

        setTitle("Bocchi the Rock! - Live Waifu Polling System");
        setSize(1250, 750); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(bgDark); 

        // Header
        JLabel headerLabel = new JLabel("Bocchi the Rock! - Live Polling", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        headerLabel.setForeground(textLight); 
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 5, 0));
        add(headerLabel, BorderLayout.NORTH);

        // --- PANEL TENGAH: GRID (KIRI) & HASIL (KANAN) ---
        JPanel mainContent = new JPanel(new BorderLayout(20, 0));
        mainContent.setBackground(bgDark);
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // 1. Panel Kiri (Grid Karakter)
        JPanel voteGrid = new JPanel(new GridLayout(2, 5, 15, 15));
        voteGrid.setBackground(bgDark);
        waifuGroup = new ButtonGroup();
        for (String name : waifus) {
            voteGrid.add(createCharacterPanel(name));
        }

        // 2. Panel Kanan (Hasil Grafik)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(panelDark);
        rightPanel.setPreferredSize(new Dimension(400, 0)); 
        rightPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(borderDark, 2), " Live Results ", 0, 0, 
            new Font("Segoe UI", Font.BOLD, 18), textLight
        ));

        resultsContainer = new JPanel();
        resultsContainer.setLayout(new BoxLayout(resultsContainer, BoxLayout.Y_AXIS));
        resultsContainer.setBackground(panelDark);
        resultsContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JScrollPane scrollResults = new JScrollPane(resultsContainer);
        scrollResults.setBorder(null);
        scrollResults.setBackground(panelDark);
        scrollResults.getViewport().setBackground(panelDark);
        scrollResults.getVerticalScrollBar().setUnitIncrement(16);
        
        rightPanel.add(scrollResults, BorderLayout.CENTER);

        // Masukkan ke mainContent 
        mainContent.add(voteGrid, BorderLayout.CENTER);
        mainContent.add(rightPanel, BorderLayout.EAST);
        add(mainContent, BorderLayout.CENTER);

        // Panel Bawah (Tombol Kontrol)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        bottomPanel.setBackground(bgDark);

        JButton btnVote = new JButton("Kirim Suara!");
        btnVote.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnVote.setPreferredSize(new Dimension(250, 60));
        btnVote.setBackground(new Color(255, 105, 180)); 
        btnVote.setForeground(Color.WHITE);
        btnVote.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVote.setFocusPainted(false);
        
        JButton btnReset = new JButton("Reset Data");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnReset.setPreferredSize(new Dimension(150, 45));
        btnReset.setBackground(new Color(220, 53, 69)); 
        btnReset.setForeground(Color.WHITE);
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.setFocusPainted(false);

        bottomPanel.add(btnVote);
        bottomPanel.add(btnReset); 
        add(bottomPanel, BorderLayout.SOUTH);

        btnVote.addActionListener(e -> submitVote());
        btnReset.addActionListener(e -> resetData());

        refreshResultsUI(); 
        setLocationRelativeTo(null);
    }

    // --- PANEL GAMBAR RESPONSIP ---
    class ResponsiveImagePanel extends JPanel {
        private Image image;
        public ResponsiveImagePanel(String path) {
            if (new File(path).exists()) this.image = new ImageIcon(path).getImage();
            setBackground(panelDark);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int pW = getWidth(), pH = getHeight();
                int iW = image.getWidth(null), iH = image.getHeight(null);
                
                double scale = Math.min((double)pW/iW, (double)pH/iH);
                int dW = (int)(iW*scale), dH = (int)(iH*scale);
                
                g2d.drawImage(image, (pW-dW)/2, (pH-dH)/2, dW, dH, null);
                g2d.dispose();
            } else {
                g.setColor(textLight);
                g.drawString("[?]", getWidth()/2 - 10, getHeight()/2);
            }
        }
    }

    private JPanel createCharacterPanel(String name) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(panelDark);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(borderDark, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        ResponsiveImagePanel img = new ResponsiveImagePanel("images/" + name.replace(" ", "_") + ".jpg");
        
        JRadioButton rb = new JRadioButton(name);
        rb.setActionCommand(name);
        rb.setBackground(panelDark);
        rb.setForeground(textLight);
        rb.setFont(new Font("Segoe UI", Font.BOLD, 14));
        rb.setHorizontalAlignment(SwingConstants.CENTER);
        rb.setFocusPainted(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        waifuGroup.add(rb);

        // --- FITUR BARU: BISA KLIK GAMBAR UNTUK MEMILIH ---
        // Menambahkan kursor tangan pada gambar agar terlihat bisa diklik
        img.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Termasuk pinggiran kotaknya

        // Menambahkan event listener agar saat panel/gambar diklik, radio button ikut terpilih
        MouseAdapter clickAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                rb.setSelected(true);
            }
        };
        img.addMouseListener(clickAdapter);
        panel.addMouseListener(clickAdapter);
        // ----------------------------------------------------

        panel.add(img, BorderLayout.CENTER);
        panel.add(rb, BorderLayout.SOUTH);
        return panel;
    }

    private void refreshResultsUI() {
        resultsContainer.removeAll(); 
        
        String sql = "SELECT nama, jumlah_vote FROM waifu_votes ORDER BY jumlah_vote DESC";
        int maxVote = 0;
        
        List<String> names = new ArrayList<>();
        List<Integer> votes = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String name = rs.getString("nama");
                int v = rs.getInt("jumlah_vote");
                names.add(name);
                votes.add(v);
                if (v > maxVote) maxVote = v;
            }
        } catch (SQLException e) { e.printStackTrace(); }

        if (maxVote == 0) maxVote = 1;

        for (int i = 0; i < names.size(); i++) {
            JPanel row = new JPanel(new BorderLayout(15, 0));
            row.setMaximumSize(new Dimension(400, 45)); 
            row.setBackground(panelDark);

            JLabel lblPhoto = new JLabel();
            String path = "images/" + names.get(i).replace(" ", "_") + ".jpg";
            if (new File(path).exists()) {
                Image img = new ImageIcon(path).getImage().getScaledInstance(35, 35, Image.SCALE_SMOOTH);
                lblPhoto.setIcon(new ImageIcon(img));
            } else {
                lblPhoto.setPreferredSize(new Dimension(35, 35));
                lblPhoto.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }

            JLabel lblName = new JLabel(names.get(i));
            lblName.setForeground(textLight);
            lblName.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lblName.setPreferredSize(new Dimension(110, 20));

            JProgressBar bar = new JProgressBar(0, maxVote);
            bar.setValue(votes.get(i));
            bar.setStringPainted(true);
            bar.setString(votes.get(i) + " suara");
            bar.setForeground(new Color(255, 105, 180));
            bar.setBackground(bgDark);
            bar.setBorderPainted(false);

            row.add(lblPhoto, BorderLayout.WEST);
            
            JPanel barWithText = new JPanel(new BorderLayout(5, 0));
            barWithText.setBackground(panelDark);
            barWithText.add(lblName, BorderLayout.WEST);
            barWithText.add(bar, BorderLayout.CENTER);
            
            row.add(barWithText, BorderLayout.CENTER);

            resultsContainer.add(row);
            resultsContainer.add(Box.createVerticalStrut(12));
        }

        resultsContainer.revalidate();
        resultsContainer.repaint();
    }

    private void setupDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS waifu_votes (nama TEXT PRIMARY KEY, jumlah_vote INTEGER DEFAULT 0)");
            stmt.execute("DELETE FROM waifu_votes WHERE nama = 'Jimihen'");
            for (String w : waifus) {
                PreparedStatement ps = conn.prepareStatement("INSERT OR IGNORE INTO waifu_votes (nama, jumlah_vote) VALUES (?, 0)");
                ps.setString(1, w);
                ps.executeUpdate();
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void submitVote() {
        if (waifuGroup.getSelection() == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih satu karakter terlebih dahulu!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String sel = waifuGroup.getSelection().getActionCommand();
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            PreparedStatement ps = conn.prepareStatement("UPDATE waifu_votes SET jumlah_vote = jumlah_vote + 1 WHERE nama = ?");
            ps.setString(1, sel);
            ps.executeUpdate();
            waifuGroup.clearSelection();
            refreshResultsUI(); 
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void resetData() {
        JPasswordField pf = new JPasswordField();
        int action = JOptionPane.showConfirmDialog(
                this, 
                pf, 
                "Masukkan Password Admin untuk mereset data:", 
                JOptionPane.OK_CANCEL_OPTION, 
                JOptionPane.WARNING_MESSAGE
        );

        if (action == JOptionPane.OK_OPTION) {
            String inputPassword = new String(pf.getPassword());
            
            if (inputPassword.equals("987654321")) {
                try (Connection conn = DriverManager.getConnection(DB_URL); Statement stmt = conn.createStatement()) {
                    stmt.execute("UPDATE waifu_votes SET jumlah_vote = 0");
                    refreshResultsUI(); 
                    JOptionPane.showMessageDialog(this, "Akses Diterima!\nSemua data voting berhasil direset.", "Reset Berhasil", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) { 
                    e.printStackTrace(); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "Password Salah! Anda tidak memiliki izin untuk mereset data.", "Akses Ditolak", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new BocchiPoll().setVisible(true));
    }
}
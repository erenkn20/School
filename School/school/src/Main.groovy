import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Ana sınıf
public class AnaSinif {
  public static void main(String[] args) {
    MenuForm menuForm = new MenuForm();
    menuForm.setVisible(true);
  }
}

// Temel sınıf
class BaseEntity {
  private static int counter = 1;
  private int id;

  public BaseEntity() {
    this.id = counter++;
  }

  public int getId() {
    return id;
  }
}

// Menü sınıfı
class MenuForm extends JFrame {
  public MenuForm() {
    setTitle("Ana Menü");
    setSize(300, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new FlowLayout());

    JButton dersButton = new JButton("Ders Formu");
    dersButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        DersForm dersForm = new DersForm();
        dersForm.setVisible(true);
      }
    });
    add(dersButton);

    JButton ogrenciButton = new JButton("Öğrenci Formu");
    ogrenciButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        OgrenciForm ogrenciForm = new OgrenciForm();
        ogrenciForm.setVisible(true);
      }
    });
    add(ogrenciButton);

    JButton ogretimGorevlisiButton = new JButton("Öğretim Görevlisi Formu");
    ogretimGorevlisiButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        OgretimGorevlisiForm ogretimGorevlisiForm = new OgretimGorevlisiForm();
        ogretimGorevlisiForm.setVisible(true);
      }
    });
    add(ogretimGorevlisiButton);
  }
}

// Ders sınıfı
class Ders extends BaseEntity {
  private String dersKodu;
  private String dersAd;
  private String dersDonem;
  private String ogretimGorevlisi;

  public Ders(String dersKodu, String dersAd, String dersDonem, String ogretimGorevlisi) {
    this.dersKodu = dersKodu;
    this.dersAd = dersAd;
    this.dersDonem = dersDonem;
    this.ogretimGorevlisi = ogretimGorevlisi;
  }

  public String getDersKodu() {
    return dersKodu;
  }

  public String getDersAd() {
    return dersAd;
  }

  public String getDersDonem() {
    return dersDonem;
  }

  public String getOgretimGorevlisi() {
    return ogretimGorevlisi;
  }

  public void kaydet(String dosyaUzantisi) {
    try (FileWriter fileWriter = new FileWriter("dersler." + dosyaUzantisi, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

      printWriter.println(getId() + "," + dersKodu + "," + dersAd + "," + dersDonem + "," + ogretimGorevlisi);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<Ders> listele(String aramaKelimesi) {
    List<Ders> dersListesi = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("dersler.txt"))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String dersKodu = parts[1];
        String dersAd = parts[2];
        String dersDonem = parts[3];
        String ogretimGorevlisi = parts[4];

        Ders ders = new Ders(dersKodu, dersAd, dersDonem, ogretimGorevlisi);
        ders.setId(id);

        if (ders.getDersAd().toLowerCase().contains(aramaKelimesi.toLowerCase())) {
          dersListesi.add(ders);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return dersListesi;
  }
}

// Ders Formu
class DersForm extends JFrame {
  public DersForm() {
    setTitle("Ders Formu");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridLayout(5, 2));

    JTextField dersKoduField = new JTextField();
    JTextField dersAdField = new JTextField();
    JTextField dersDonemField = new JTextField();
    JTextField ogretimGorevlisiField = new JTextField();

    add(new JLabel("Ders Kodu: "));
    add(dersKoduField);
    add(new JLabel("Ders Adı: "));
    add(dersAdField);
    add(new JLabel("Ders Dönemi: "));
    add(dersDonemField);
    add(new JLabel("Öğretim Görevlisi: "));
    add(ogretimGorevlisiField);

    JButton kaydetButton = new JButton("Kaydet");
    kaydetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Ders ders = new Ders(
                dersKoduField.getText(),
                dersAdField.getText(),
                dersDonemField.getText(),
                ogretimGorevlisiField.getText()
        );

        ders.kaydet("csv"); // Dosya uzantısını istediğiniz gibi değiştirebilirsiniz
        JOptionPane.showMessageDialog(null, "Ders başarıyla kaydedildi.");
      }
    });

    add(kaydetButton);
  }
}

// Öğrenci sınıfı
class Ogrenci extends BaseEntity {
  private String ogrenciNo;
  private String ogrenciAd;
  private String ogrenciSoyad;
  private String ogrenciBolum;
  private List<String> ogrenciDersler;

  public Ogrenci(String ogrenciNo, String ogrenciAd, String ogrenciSoyad, String ogrenciBolum) {
    this.ogrenciNo = ogrenciNo;
    this.ogrenciAd = ogrenciAd;
    this.ogrenciSoyad = ogrenciSoyad;
    this.ogrenciBolum = ogrenciBolum;
    this.ogrenciDersler = new ArrayList<>();
  }

  public String getOgrenciNo() {
    return ogrenciNo;
  }

  public String getOgrenciAd() {
    return ogrenciAd;
  }

  public String getOgrenciSoyad() {
    return ogrenciSoyad;
  }

  public String getOgrenciBolum() {
    return ogrenciBolum;
  }

  public List<String> getOgrenciDersler() {
    return ogrenciDersler;
  }

  public void ogrenciDersEkle(String ders) {
    ogrenciDersler.add(ders);
  }

  public void kaydet(String dosyaUzantisi) {
    try (FileWriter fileWriter = new FileWriter("ogrenciler." + dosyaUzantisi, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

      StringBuilder dersler = new StringBuilder();
      for (String ders : ogrenciDersler) {
        dersler.append(ders).append(",");
      }

      printWriter.println(getId() + "," + ogrenciNo + "," + ogrenciAd + "," + ogrenciSoyad + "," + ogrenciBolum + "," + dersler);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<Ogrenci> listele(String aramaKelimesi) {
    List<Ogrenci> ogrenciListesi = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("ogrenciler.txt"))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String ogrenciNo = parts[1];
        String ogrenciAd = parts[2];
        String ogrenciSoyad = parts[3];
        String ogrenciBolum = parts[4];
        List<String> ogrenciDersler = new ArrayList<>();
        for (int i = 5; i < parts.length; i++) {
          ogrenciDersler.add(parts[i]);
        }

        Ogrenci ogrenci = new Ogrenci(ogrenciNo, ogrenciAd, ogrenciSoyad, ogrenciBolum);
        ogrenci.setId(id);
        ogrenci.ogrenciDersler = ogrenciDersler;

        if (ogrenci.getOgrenciAd().toLowerCase().contains(aramaKelimesi.toLowerCase())) {
          ogrenciListesi.add(ogrenci);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return ogrenciListesi;
  }
}

// Öğrenci Formu
class OgrenciForm extends JFrame {
  public OgrenciForm() {
    setTitle("Öğrenci Formu");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridLayout(5, 2));

    JTextField ogrenciNoField = new JTextField();
    JTextField ogrenciAdField = new JTextField();
    JTextField ogrenciSoyadField = new JTextField();
    JTextField ogrenciBolumField = new JTextField();

    add(new JLabel("Öğrenci No: "));
    add(ogrenciNoField);
    add(new JLabel("Öğrenci Adı: "));
    add(ogrenciAdField);
    add(new JLabel("Öğrenci Soyadı: "));
    add(ogrenciSoyadField);
    add(new JLabel("Öğrenci Bölümü: "));
    add(ogrenciBolumField);

    JButton kaydetButton = new JButton("Kaydet");
    kaydetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Ogrenci ogrenci = new Ogrenci(
                ogrenciNoField.getText(),
                ogrenciAdField.getText(),
                ogrenciSoyadField.getText(),
                ogrenciBolumField.getText()
        );

        ogrenci.kaydet("csv"); // Dosya uzantısını istediğiniz gibi değiştirebilirsiniz
        JOptionPane.showMessageDialog(null, "Öğrenci başarıyla kaydedildi.");
      }
    });

    add(kaydetButton);
  }
}

// Öğretim Görevlisi sınıfı
class OgretimGorevlisi extends BaseEntity {
  private String ogretmenNo;
  private String ad;
  private String soyad;
  private String bolum;
  private List<String> verdigiDersler;

  public OgretimGorevlisi(String ogretmenNo, String ad, String soyad, String bolum) {
    this.ogretmenNo = ogretmenNo;
    this.ad = ad;
    this.soyad = soyad;
    this.bolum = bolum;
    this.verdigiDersler = new ArrayList<>();
  }

  public String getOgretmenNo() {
    return ogretmenNo;
  }

  public String getAd() {
    return ad;
  }

  public String getSoyad() {
    return soyad;
  }

  public String getBolum() {
    return bolum;
  }

  public List<String> getVerdigiDersler() {
    return verdigiDersler;
  }

  public void dersEkle(String ders) {
    verdigiDersler.add(ders);
  }

  public void kaydet(String dosyaUzantisi) {
    try (FileWriter fileWriter = new FileWriter("ogretimGorevlileri." + dosyaUzantisi, true);
         BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
         PrintWriter printWriter = new PrintWriter(bufferedWriter)) {

      StringBuilder dersler = new StringBuilder();
      for (String ders : verdigiDersler) {
        dersler.append(ders).append(",");
      }

      printWriter.println(getId() + "," + ogretmenNo + "," + ad + "," + soyad + "," + bolum + "," + dersler);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static List<OgretimGorevlisi> listele(String aramaKelimesi) {
    List<OgretimGorevlisi> ogretimGorevlisiListesi = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader("ogretimGorevlileri.txt"))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0]);
        String ogretmenNo = parts[1];
        String ad = parts[2];
        String soyad = parts[3];
        String bolum = parts[4];
        List<String> verdigiDersler = new ArrayList<>();
        for (int i = 5; i < parts.length; i++) {
          verdigiDersler.add(parts[i]);
        }

        OgretimGorevlisi ogretimGorevlisi = new OgretimGorevlisi(ogretmenNo, ad, soyad, bolum);
        ogretimGorevlisi.setId(id);
        ogretimGorevlisi.verdigiDersler = verdigiDersler;

        if (ogretimGorevlisi.getAd().toLowerCase().contains(aramaKelimesi.toLowerCase())) {
          ogretimGorevlisiListesi.add(ogretimGorevlisi);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return ogretimGorevlisiListesi;
  }
}

// Öğretim Görevlisi Formu
class OgretimGorevlisiForm extends JFrame {
  public OgretimGorevlisiForm() {
    setTitle("Öğretim Görevlisi Formu");
    setSize(400, 300);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new GridLayout(5, 2));

    JTextField ogretmenNoField = new JTextField();
    JTextField adField = new JTextField();
    JTextField soyadField = new JTextField();
    JTextField bolumField = new JTextField();

    add(new JLabel("Öğretmen No: "));
    add(ogretmenNoField);
    add(new JLabel("Ad: "));
    add(adField);
    add(new JLabel("Soyad: "));
    add(soyadField);
    add(new JLabel("Bölüm: "));
    add(bolumField);

    JButton kaydetButton = new JButton("Kaydet");
    kaydetButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        OgretimGorevlisi ogretimGorevlisi = new OgretimGorevlisi(
                ogretmenNoField.getText(),
                adField.getText(),
                soyadField.getText(),
                bolumField.getText()
        );

        ogretimGorevlisi.kaydet("csv"); // Dosya uzantısını istediğiniz gibi değiştirebilirsiniz
        JOptionPane.showMessageDialog(null, "Öğretim Görevlisi başarıyla kaydedildi.");
      }
    });

    add(kaydetButton);
  }
}


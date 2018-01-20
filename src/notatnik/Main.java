package notatnik;

  /**
     * Import bibliotek używanych w aplikacji
     */


    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.*;
    import java.io.File;
    import java.io.FileNotFoundException;
    import java.io.IOException;
    import java.io.PrintWriter;
    import java.util.Scanner;
    import java.util.logging.Level;
    import java.util.logging.Logger;
    import static javax.imageio.ImageIO.getCacheDirectory;

public class Main extends JFrame {

    /**
     * Deklaracja zmiennych użytych w aplikacji
     */
    
    private int x = (int)this.getX();
    private int y = (int)this.getY();
    
    private int scrWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
    private int scrHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
   
    
    private String nazwaPliku = "Nowy dokument";
    private boolean czyZmodyfikowany = false;
    private String sciezkaDoPliku = "";
    
    private int memorySlot = 0;
    
    private boolean czyZapisany = false;
    
    private JMenuBar pasekMenu = new JMenuBar();
    
        private JMenu mFile = new JMenu("File");
        private JMenu mEdit = new JMenu("Edit");
        private JMenu mFormat = new JMenu("Format");
        private JMenu mView = new JMenu("View");
        private JMenu mHelp = new JMenu("Help");
        
    private JTextArea poleTekstowe = new JTextArea(); 
    private String[] pamiecPodreczna = new String[100];
    
    private JScrollPane poleTekstowePrzewijane = new JScrollPane(poleTekstowe, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    private JMenuBar pasekStanu = new JMenuBar();
    private JLabel createdBy = new JLabel("by Emil Karpowicz / Styczeń 2018  ");    
    private JFrame ramkaGlowna = this;
    
       
    /**
     * Główny konstruktor klasy Main
     */
    
    public Main() throws FileNotFoundException, IOException
    {
        initComponents();
    }
    
     /**
     * Metoda inicializująca poszczgólne komponenty aplikacji
     */
    
    public void initComponents() throws FileNotFoundException, IOException
    {
        /*
            Definiowanie podstawowych własności okna / widoku głównego aplikacji
        */
        
        this.setBounds(scrWidth / 6, scrHeight / 4, scrWidth / 4, scrHeight / 2);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        nowyDokument();
                      
        /*
            Budowanie menu głównego aplikacji
        */
        
        pasekMenu.add(mFile).setMnemonic('F');
            mFile.add(new elementMenu("Nowy", "Tworzy nowy dokument", "ctrl N", KeyEvent.VK_N));
            mFile.add(new elementMenu("Otwórz...", "Otwiera dokument ze wskazanej lokalizacji", "ctrl O", KeyEvent.VK_O));
            mFile.add(new elementMenu("Zapisz", "Zapisuje dokument w domyślnej lokalizacji", "ctrl S", KeyEvent.VK_Z));
            mFile.add(new elementMenu("Zapisz jako...", "Zapisuje dokument we wskazanej lokalizacji", "", KeyEvent.VK_J));
            mFile.addSeparator();
            mFile.add(new elementMenu("Ustawienia strony...", "Wywołuje okno z ustawieniami strony", "", KeyEvent.VK_U));
                mFile.getItem(5).setEnabled(false);
            mFile.add(new elementMenu("Drukuj", "Drukuje dokument", "ctrl P", KeyEvent.VK_D));
                mFile.getItem(6).setEnabled(false);
            mFile.addSeparator();
            mFile.add(new elementMenu("Zakończ", "Zamyka aplikacje", "", KeyEvent.VK_A));
        pasekMenu.add(mEdit).setMnemonic('E');
            mEdit.add(new elementMenu("Cofnij", "Przywraca ostatnio usunięty znak", "ctrl Z", KeyEvent.VK_C));
            mEdit.addSeparator();
            mEdit.add(new elementMenu("Wytnij", "Wycina zaznaczony fragment tekstu", "ctrl X", KeyEvent.VK_W));
            mEdit.add(new elementMenu("Kopiuj", "Kopiuje zaznaczony fragment tekstu", "ctrl C", KeyEvent.VK_K));
            mEdit.add(new elementMenu("Wklej", "Wkleja skopiowany fragment tekstu", "ctrl V", KeyEvent.VK_E));
            mEdit.add(new elementMenu("Usuń", "Usuwa zaznaczony fragment tekstu","", KeyEvent.VK_U));
            mEdit.addSeparator();
            mEdit.add(new elementMenu("Znajdź...", "Wyszukuje w tekscie wybrany fragment tekstu ", "ctrl F", KeyEvent.VK_Z));
            mEdit.add(new elementMenu("Zamień...", "Zamienia wybrany fragment tekstu na inny", "ctrl H", KeyEvent.VK_A));
                mEdit.getItem(8).setEnabled(false);
            mEdit.add(new elementMenu("Przejdź do...", "Przenosi kursor do wybranego fragmentu tekstu", "", KeyEvent.VK_P));
                mEdit.getItem(9).setEnabled(false);
            mEdit.addSeparator();
            mEdit.add(new elementMenu("Zaznacz wszystko", "Zaznacza cały tekst", "ctrl A", KeyEvent.VK_R));
            mEdit.addSeparator();
            mEdit.add(new elementMenu("Data i godzina", "Zwraca aktualną datę i godzinę", "", KeyEvent.VK_G));
                mEdit.getItem(13).setEnabled(false);
            pasekMenu.add(mFormat).setMnemonic('o');
            mFormat.add("Zawijanie wierszy");
                mFormat.getItem(0).setEnabled(false);
            mFormat.add("Czcionka...");
                mFormat.getItem(1).setEnabled(false);
        pasekMenu.add(mView).setMnemonic('V');
            mView.add("Pasek stanu");
                mView.getItem(0).setEnabled(false);
        pasekMenu.add(mHelp).setMnemonic('H');
            mHelp.add("Wyświetl pomoc");
                mHelp.getItem(0).setEnabled(false);
            mHelp.add("Info");
                mHelp.getItem(1).setEnabled(false);
        
        /*
            Definowanie zachowania aplikacji powiązanego z wykonaniem 
            akcji na poszczególnych elementach menu
        */
        
            // Tworzenie nowego dokumentu
            
            mFile.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    nowyDokument();
                } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                } catch (IOException ex) {
                    System.out.println("Wystąpienie błędu IOException");
                }
            }
            });
            
            // Odczyt dokumentu z istniejącego pliku
            
            mFile.getItem(1).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    odczytZPliku();
                } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                }
            }
            });
            
             // Zapis dokumentu do istniejącego pliku
            
            mFile.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    zapisz();
                } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                }
            }
            });
            
             // Zapis dokumentu do istniejącego pliku
            
            mFile.getItem(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    zapiszJako();
                } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                }
            }
            });
            
             // Zamkniecie dokumentu
             
             this.addWindowListener(new WindowAdapter() 
             {
             public void windowClosing(WindowEvent e) {
             if (czyZmodyfikowany == true && czyZapisany == false) try {
                    czyZapisacZmiany();
                    System.exit(0);   
                            } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                }
             else System.exit(0);    
             }
             });
             
            
            mFile.getItem(8).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (czyZmodyfikowany == true && czyZapisany == false) try {
                    czyZapisacZmiany();
                    System.exit(0); 
                            } catch (FileNotFoundException ex) {
                    System.out.println("Wystąpienie błędu FileNotFoundException");
                }System.exit(0);
  
            }
            });
            
             // Cofnięcie wprowadzonego znaku / wyrażenia
            
            mEdit.getItem(0).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            
            if (memorySlot != 101)    
            poleTekstowe.setText(pamiecPodreczna[memorySlot++]);               
            else
            JOptionPane.showMessageDialog(rootPane, "Nie możesz już dalej cofnąć!");
            
            
            } 
            
            });
            
            // Wycięcie zaznaczonego fragmentu tekstu
            
            mEdit.getItem(2).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                poleTekstowe.cut();

            }
            });
            
            // Skopiowanie do schowka zaznaczonego fragmentu tekstu
            
            mEdit.getItem(3).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                poleTekstowe.copy();

            }
            });
            
            // Wklejenie znajdującego się w schowku fragmentu tekstu
            
            mEdit.getItem(4).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                poleTekstowe.paste();

            }
            });
            
            // Skasowanie zaznaczonego fragmentu tekstu
            
            mEdit.getItem(5).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                poleTekstowe.cut();

            }
            });
            
             // Wyszukanie w tekście wskazanego wyrażenia
            
            mEdit.getItem(7).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

             
               SearchFrame noweZapytanie = new SearchFrame(ramkaGlowna); 
               noweZapytanie.setVisible(true);
             
                                
            }
            });
            
             // Zaznaczenie całego tekstu znajdującego się w polu tekstowym
            
            mEdit.getItem(11).addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                poleTekstowe.selectAll();

            }
            });
            
        /*
            Dodanie menu głównego, obszaru tekstowego oraz paska stanu
            do okna / widoku głównego aplikacji
        */
       
              this.getContentPane().add(pasekMenu, BorderLayout.NORTH);
                poleTekstowe.setLineWrap(true);
              this.getContentPane().add(poleTekstowePrzewijane, BorderLayout.CENTER);   
                pasekStanu.setLayout(new BorderLayout());
                pasekStanu.setSize(scrWidth, 25);
                pasekStanu.add(createdBy, BorderLayout.EAST);
              this.getContentPane().add(pasekStanu, BorderLayout.SOUTH);

        /*
            Dodanie listenera obszaru tekstowego, który sprawdza 
            czy dokument został zmodyfuikowany
        */
        
      
        poleTekstowe.addKeyListener(new KeyAdapter() {
            
            public void keyTyped(KeyEvent e) {
                
                    if (czyToAscii(e.getKeyChar()) == true) 
                        czyZmodyfikowany = true;   
                                 
                    
                }

            public void keyPressed(KeyEvent e) {
                
                if (czyToAscii(e.getKeyChar()) == true)
                    if (!(e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z))
                    {
                    czyZmodyfikowany = true;
                    memorySlot = 0;
                    for (int i = 99; i > 0; --i)
                        {   
                            pamiecPodreczna[i] = pamiecPodreczna[i-1];
                        }
                        pamiecPodreczna[0] = poleTekstowe.getText();                   
                    }
                
                             
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_V)
                    System.out.println("Coś zostało wklejone");
            }
        });
    }
    
    /**
     * Klasa nadająca komponentom menu nazwę, opis, skrót klawiszowy i mnemonic key
     */
    
    public class elementMenu extends AbstractAction
    {
        public elementMenu(String nazwa, String krotkiOpis, String skrotKlawiszowy, int mnemonicKey)
        {
            this.putValue(Action.NAME, nazwa);
            this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(skrotKlawiszowy));
            this.putValue(Action.SHORT_DESCRIPTION, krotkiOpis);
            this.putValue(Action.MNEMONIC_KEY, mnemonicKey);
            this.putValue(Action.NAME, nazwa);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {

        }
        
    }
    
    /**
    * Metoda służąca do tworzenia nowego dokumentu
    */
    
    private void nowyDokument() throws FileNotFoundException, IOException
    {
        if (czyZmodyfikowany == true) czyZapisacZmiany();
        File nowyPlik = new File(nazwaPliku);
        this.setTitle(nowyPlik.getName() + " - JavaPad");
        poleTekstowe.setText(null);
        czyZmodyfikowany = false;
        czyZapisany = false;
        sciezkaDoPliku = nowyPlik.getPath();
        
    }
    
    /**
    * Metoda służąca otwierania dokumentu z istniejącego pliku
    */
    
    public void odczytZPliku() throws FileNotFoundException
    {
        if (czyZmodyfikowany == true) czyZapisacZmiany();
                    
        poleTekstowe.setText(null);
         
        JFileChooser wybierzPlik = new JFileChooser(sciezkaDoPliku);
        wybierzPlik.showOpenDialog(pasekMenu);
        File plikDoOdczytu = wybierzPlik.getSelectedFile();
        Scanner otworz = new Scanner(plikDoOdczytu);
        while (otworz.hasNextLine())
        {
            poleTekstowe.append(otworz.nextLine()+"\n");
        }
        
        this.setTitle(plikDoOdczytu.getName() + " - JavaPad");
        czyZmodyfikowany = false;
        czyZapisany = true;
        sciezkaDoPliku = wybierzPlik.getSelectedFile().getPath();
       
    }
    
    /**
    * Metoda służąca do zapisu dokumentu bez wskazania lokalizacji
    */
    
    public void zapisz() throws FileNotFoundException
    {
        
        File nowyPlik = new File(sciezkaDoPliku);
        if (nowyPlik.isFile()) 
        {
            int wybierzOpcje = JOptionPane.showConfirmDialog(rootPane, "Ten plik już istnieje, czy chcesz go nadpisać");
                { 
                    if (wybierzOpcje == 0) 
                        {
                        PrintWriter zapisz = new PrintWriter(nowyPlik.getAbsolutePath());
                        zapisz.print(poleTekstowe.getText());
                        zapisz.close();
                        this.setTitle(nowyPlik.getName() + " - JavaPad");
                        czyZmodyfikowany = false;
                        czyZapisany = true;
                        sciezkaDoPliku = nowyPlik.getPath();  
                        }
                    else if (wybierzOpcje == 1) zapiszJako();
                    
                }
        }   
        
    }
    
    /**
    * Metoda służąca do zapisu dokumentu ze wskazaniem lokalizacji
    */
    
    public void zapiszJako() throws FileNotFoundException
    {
        JFileChooser wybierzPlik = new JFileChooser(sciezkaDoPliku);
        wybierzPlik.showSaveDialog(wybierzPlik);
        File nowyPlik = wybierzPlik.getSelectedFile();
        PrintWriter zapisz = new PrintWriter(nowyPlik.getAbsolutePath());
        zapisz.print(poleTekstowe.getText());
        zapisz.close();
        this.setTitle(nowyPlik.getName() + " - JavaPad");
        czyZmodyfikowany = false;
        czyZapisany = true;
        sciezkaDoPliku = wybierzPlik.getSelectedFile().getPath();
    }
   
     private boolean czyToAscii(char zn)
     {
         for (int i = 0; i < 256; i++)
             if (zn == i)
                 return true;
             
         return false;
     }
     
    /**
    * Metoda służąca do wyszukania danego słowa w tekście
    */
     
     public class SearchFrame extends JFrame
     {
         private JLabel komunikat = new JLabel("Podaj wyrażenie, którego szukasz: ");
         private JPanel panelZnajdz = new JPanel();
         private JButton bZnajdz = new JButton("Wyszukaj"); 
         private JButton bNastepny = new JButton("Następny");         
         private JTextArea szukaneWyrazenie = new JTextArea(3, 30);         
         private int poczatekSzukanego = 0;
         
            public SearchFrame(JFrame parent)
            {

                this.setBounds(parent.getX()+parent.getWidth()/3,parent.getY()+parent.getHeight()/3, 350, 140);
                this.setTitle("Znajdowanie i zamienianie");
                this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                this.add(panelZnajdz);
                panelZnajdz.add(komunikat, BorderLayout.NORTH);
                panelZnajdz.add(szukaneWyrazenie, BorderLayout.CENTER);
                panelZnajdz.add(bZnajdz, BorderLayout.SOUTH);
                panelZnajdz.add(bNastepny, BorderLayout.SOUTH);

                bZnajdz.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        poczatekSzukanego = poleTekstowe.getText().indexOf(szukaneWyrazenie.getText()); 
                        if (poczatekSzukanego >= 0 && szukaneWyrazenie.getText().length() > 0)
                        poleTekstowe.select(poczatekSzukanego, poczatekSzukanego + szukaneWyrazenie.getText().length());
                    }
                });
                
                bNastepny.addActionListener(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        poczatekSzukanego = poleTekstowe.getText().indexOf(szukaneWyrazenie.getText(), poczatekSzukanego + szukaneWyrazenie.getText().length());
                        if (poczatekSzukanego >= 0 && szukaneWyrazenie.getText().length() > 0)
                        {
                        poleTekstowe.requestFocus();
                        poleTekstowe.select(poczatekSzukanego, poczatekSzukanego + szukaneWyrazenie.getText().length());
                        }
                   }
                });
            } 
     }  
     
     /**
     * Metoda pytająca czy zapisać zmian
     */    
    
    public void czyZapisacZmiany() throws FileNotFoundException
    {
        int wybierzOpcje = JOptionPane.showConfirmDialog(rootPane, "Czy zapisać zmiany?");
            if (wybierzOpcje == 0) zapiszJako();
    }
    
    public void tenPlikJuzIstnieje() throws FileNotFoundException
    {
        int wybierzOpcje = JOptionPane.showConfirmDialog(rootPane, "Ten plik już istnieje, czy chcesz go nadpisać");
            if (wybierzOpcje == 0) zapisz();
    }
        
    /**
     * Główna metoda aplikacji
     */    
    
    public static void main(String[] args) throws FileNotFoundException, IOException {
        new Main().setVisible(true);
    }
    
}
    
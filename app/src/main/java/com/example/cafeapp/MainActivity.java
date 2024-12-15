package com.example.cafeapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView menuTextView, totalPriceTextView, invoiceTextView;
    EditText quantityEditText;
    GridView menuGridView;

    ArrayList<String> commandes = new ArrayList<>();
    ArrayList<Integer> quantites = new ArrayList<>();
    double prixTotal = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuTextView = findViewById(R.id.menuTextView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        invoiceTextView = findViewById(R.id.invoiceTextView);
        quantityEditText = findViewById(R.id.quantityEditText);
        menuGridView = findViewById(R.id.menuGridView);


        String[] items = {
                "Café noir (8 MAD)", "Café au lait (9 MAD)", "Thé à la Menthe (8 MAD)",
                "Jus d'Orange (13 MAD)", "Jus de Citron (13 MAD)", "Hawai (10 MAD)",
                "Top's (10 MAD)", "Ice (10 MAD)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        menuGridView.setAdapter(adapter);

        menuGridView.setOnItemClickListener((parent, view, position, id) -> {
            try {
                String selectedItem = items[position];
                int quantity = Integer.parseInt(quantityEditText.getText().toString());
                String itemName = selectedItem.split(" \\(")[0];
                double prix = Double.parseDouble(selectedItem.split(" \\(")[1].split(" MAD\\)")[0]);
                ajouterCommande(itemName, prix, quantity);
            } catch (NumberFormatException e) {
                // Handle the case where the quantity is not a valid number
                invoiceTextView.setText("Invalid quantity entered.");
            }
        });
    }

    private void ajouterCommande(String nom, double prix, int quantite) {
        commandes.add(nom);
        quantites.add(quantite);
        prixTotal += prix * quantite;
        totalPriceTextView.setText("Total : " + prixTotal + " MAD");
    }

    public void viderFacture(View view) {
        invoiceTextView.setText("");
    }

    public void reinitialiserTotal(View view) {
        prixTotal = 0;
        totalPriceTextView.setText("Total: 0 MAD");
        commandes.clear(); // Clear the order list
        quantites.clear(); // Clear the quantities list
    }

    public void imprimerFacture(View view) {
        if (commandes.isEmpty()) {
            invoiceTextView.setText("Aucune commande passée.");
            return;
        }

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault());
        String formattedDateTime = formatter.format(now);

        StringBuilder facture = new StringBuilder();
        facture.append("--------------- Facture ---------------\n");
        facture.append("  Date et heure : ").append(formattedDateTime).append("  \n");
        facture.append("------------------------------\n");

        for (int i = 0; i < commandes.size(); i++) {
            String commande = commandes.get(i);
            int quantite = quantites.get(i);
            double prixUnitaire = 0;
            switch (commande) {
                case "Café noir": prixUnitaire = 8; break;
                case "Café au lait": prixUnitaire = 9; break;
                case "Thé à la Menthe": prixUnitaire = 8; break;
                case "Jus d'Orange": prixUnitaire = 13; break;
                case "Jus de Citron": prixUnitaire = 13; break;
                case "Hawai": prixUnitaire = 10; break;
                case "Top's": prixUnitaire = 10; break;
                case "Ice": prixUnitaire = 10; break;
            }
            double prixLigne = prixUnitaire * quantite;
            facture.append(commande).append(" x ").append(quantite).append(" : ").append(prixLigne).append(" MAD\n");
        }

        facture.append("-----------------------\n");
        facture.append("Total : ").append(prixTotal).append(" MAD");

        invoiceTextView.setText(facture.toString());
    }
}
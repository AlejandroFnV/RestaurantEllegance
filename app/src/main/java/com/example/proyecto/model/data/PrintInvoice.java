package com.example.proyecto.model.data;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.PrintManager;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;

import com.example.proyecto.R;
import com.example.proyecto.model.repository.InvoiceRepository;
import com.example.proyecto.model.repository.OrderRepository;
import com.example.proyecto.model.repository.ProductRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PrintInvoice extends AppCompatActivity {

    private long idInvoice;
    private Invoice invoice;
    private List<Order> orderList;
    private ProductList productList = new ProductList();
    private OrderRepository orderRepository = new OrderRepository();
    private ProductRepository productRepository = new ProductRepository();
    private InvoiceRepository invoiceRepository = new InvoiceRepository();

    private int indice = 10;
    private int inicio = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idInvoice = getIntent().getLongExtra("idInvoice", -1);
        if (idInvoice != -1){
            setInvoice();
        }
    }

    private void setInvoice() {
        invoiceRepository.getByIdInvoice(idInvoice);
        invoiceRepository.getInvoiceById().observe(this, new Observer<Invoice>() {
            @Override
            public void onChanged(Invoice invoice2) {
                invoice = invoice2;
                setOrders();
            }
        });
    }

    private void setOrders() {
        orderRepository.getByInvoice(idInvoice);
        orderRepository.getGetByInvoiceLiveData().observe(this, new Observer<ArrayList<Order>>() {
            @Override
            public void onChanged(ArrayList<Order> orders) {
                orderList = orders;
                setProducts();
            }
        });
    }

    private void setProducts() {
        productRepository.index();
        productRepository.getIndexLiveData().observe(PrintInvoice.this, new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                for (Order order : orderList){
                    for(Product product : products){
                        if (order.getIdProduct() == product.getId()){
                            order.setProduct_name(product.getName());
                            productList.add(product);
                        }

                        Log.v("producto", product.toString());
                    }
                }

                doPrint(productList);
            }
        });
    }

    private void doPrint(ProductList productList) {
        //Creamos una instancia de printManager
        PrintManager printManager = (PrintManager) this
                .getSystemService(Context.PRINT_SERVICE);

        //Asignamo un nombre al job
        String jobName = this.getString(R.string.app_name) + " Document";

        // Start a print job, passing in a PrintDocumentAdapter implementation
        // to handle the generation of a print document
        printManager.print(jobName, new MyPrintDocumentAdapter(this, productList),
                null);
    }
    private class MyPrintDocumentAdapter extends PrintDocumentAdapter {

        private Context context;
        private PrintedPdfDocument myPdfDocument;
        private int pageHeight, pageWidth, totalpages = 0;
        ProductList productList2;//habria que contar el total de páginas

        public MyPrintDocumentAdapter(PrintInvoice mainActivity, ProductList productList) {
            this.context = mainActivity;
            this.productList2 = productList;
        }

        @Override
        public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
            myPdfDocument = new PrintedPdfDocument(context,
                    newAttributes);
            pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72; //Mils -> tamaño de la hoja en milésimas de pulgada
            pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;

            if (cancellationSignal.isCanceled() ) {
                callback.onLayoutCancelled();
                return;
            }
            totalpages=pageNumber();
            Log.d ("antes",String.valueOf(totalpages));
            if (totalpages > 0) {
                PrintDocumentInfo.Builder builder = new
                        PrintDocumentInfo
                                .Builder("print_output.pdf")
                        .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                        .setPageCount(totalpages);
                PrintDocumentInfo info = builder.build();
                callback.onLayoutFinished(info, true);
            } else {
                callback.onLayoutFailed("Page count is zero.");
            }
        }

        public int pageNumber(){
            Log.d("antes", "antes en pagenumber");
            int value;
            for(Product comanda : productList2.getAll()){
                Log.v("paginas", "Comanda: " + comanda.toString());
            }
            Log.v("paginas", "Texto: " + productList.size());
            Log.v("paginas", "Texto2: " + productList.getAll().size());
            if ((orderList.size() / 10f) % 1 == 0) {
                value = (int) (orderList.size() / 10);
            } else {
                value = (int) (orderList.size() / 10);
                value = value + 1;
            }
            Log.v("hola", "Value: " + value);
            return value;
        }

        @Override
        public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
            PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                    pageHeight, totalpages).create();
            for(int i = 0; i < pageNumber();i++){
                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                PdfDocument.Page page = myPdfDocument.startPage(i);
                drawPage(page);
                myPdfDocument.finishPage(page);
            }



            try {
                myPdfDocument.writeTo(new FileOutputStream(
                        destination.getFileDescriptor()));
            } catch (IOException e) {
                callback.onWriteFailed(e.toString());
                return;
            } finally {
                myPdfDocument.close();
                myPdfDocument = null;
            }
            callback.onWriteFinished(pages);
        }

        private void drawPage(PdfDocument.Page page) {
            Canvas canvas = page.getCanvas();

            // units are in points (1/72 of an inch)
            int lineBaseLine = 72;
            int leftMargin = 54;

            Date currentTime = Calendar.getInstance().getTime();


            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
           /*if (page.getInfo().getPageNumber() > 0){
                indice = 19;
                Log.v("freckk", "HOLAAA " + "Page info y to eso" + page.getInfo().getPageNumber() + "INDICE " + indice);
            }*/
            if (page.getInfo().getPageNumber() == 0) {
                paint.setTextSize(36);
                canvas.drawText("Restaurant Ellegance", canvas.getWidth() / 4, lineBaseLine, paint);

                lineBaseLine = lineBaseLine + 15;
                paint.setTextSize(25);
                canvas.drawText("C\\ Falsa S.N", canvas.getWidth() / 3 + 34, lineBaseLine + 25, paint);

                lineBaseLine = lineBaseLine + 75;
                paint.setTextSize(20);
                canvas.drawText("Nombre Cliente:", leftMargin, lineBaseLine, paint);

                lineBaseLine = lineBaseLine + 45;
                canvas.drawText("Direccion:", leftMargin, lineBaseLine, paint);

                lineBaseLine = lineBaseLine + 45;
                canvas.drawText("NIF:", leftMargin, lineBaseLine, paint);

                lineBaseLine = lineBaseLine + 35;
                canvas.drawText("-----------------------------------------------------------------------------------", leftMargin, lineBaseLine, paint);

                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd-mm-yy");
                String hora = "Fecha: " + invoice.getClose();
                String idFactura = "ID: " + invoice.getId();
                String atendidoPor = "Empleado: " +  invoice.getIdCloseEmployee();

                lineBaseLine = lineBaseLine + 30;

                canvas.drawText(idFactura, leftMargin, lineBaseLine, paint);
                canvas.drawText(atendidoPor, leftMargin * 2 + 15, lineBaseLine, paint);
                canvas.drawText(hora, leftMargin * 5 + 30, lineBaseLine, paint);


                lineBaseLine = lineBaseLine + 30;
                canvas.drawText("-----------------------------------------------------------------------------------", leftMargin, lineBaseLine, paint);
            }

            int separacionCols = 150;
            String unidades = "Ud."; int unitsCol = leftMargin + 0;
            String nombre = "Nombre"; int nombreCol = unitsCol + separacionCols + 6;
            String precio = "Precio"; int priceCol = nombreCol + separacionCols;
            String total = "Total"; int totCol = priceCol + separacionCols;

            lineBaseLine = lineBaseLine + 30;
            canvas.drawText(unidades, unitsCol, lineBaseLine, paint);
            canvas.drawText(nombre, nombreCol - 100, lineBaseLine, paint);
            canvas.drawText(precio, priceCol, lineBaseLine, paint);
            canvas.drawText(total, totCol, lineBaseLine, paint);

            lineBaseLine = lineBaseLine + 30;
            paint.setTextSize(20);
            canvas.drawText("-----------------------------------------------------------------------------------", leftMargin, lineBaseLine, paint);


            int pagina = page.getInfo().getPageNumber() + 1;
            Log.d("pollo", "Pagina" + page.getInfo().getPageNumber());
            for (int i = inicio;i<indice*pagina && i< orderList.size();i++){
                Order comanda = orderList.get(i);
                Log.d("pagina", "Comanda" + comanda.toString());
                int units = comanda.getUnits();
                Log.v("promo" , "Comanda");
                for(Product product : productList2.getAll()){
                    Log.v("promo", "Producto " + product.toString());
                }

                String productName =  comanda.getProduct_name();
                Log.v("promo", "Producto " + productName);
                float price = productList.get(comanda.getIdProduct()).getPrice();
                Log.v("hola", "comanda: " + comanda.toString());
                lineBaseLine = lineBaseLine + 30;
                canvas.drawText(String.valueOf(units), unitsCol, lineBaseLine, paint);
                canvas.drawText(productName, nombreCol - 100, lineBaseLine, paint);
                canvas.drawText(String.valueOf(price), priceCol, lineBaseLine, paint);
                canvas.drawText(String.valueOf(price * units), totCol, lineBaseLine, paint);
            }
            Log.v("inicio", inicio + "");
            inicio = inicio + 10;

            lineBaseLine = lineBaseLine + 30;
            canvas.drawText("-----------------------------------------------------------------------------------", leftMargin, lineBaseLine, paint);
            lineBaseLine = lineBaseLine + 30;
            canvas.drawText(total + ": " + invoice.getTotal(), totCol - 65, lineBaseLine, paint);

        }
    }
}

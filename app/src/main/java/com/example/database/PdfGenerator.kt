package com.example.database

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.widget.Toast
import com.google.firebase.inappmessaging.model.ImageData
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Chunk
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Element
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfPTable
import java.io.File
import java.io.FileOutputStream
//import com.itextpdf.text.
import com.itextpdf.text.pdf.PdfDocument
import java.io.ByteArrayOutputStream

class PdfGenerator {

    // Method to generate PDF from database data
    fun generatePdfFromData(context: Context, data: List<DataClass>) {
        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "database_data.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PENJUALAN", titleFont)
            title.alignment = Element.ALIGN_CENTER
////            document.add(Image("../drawable/foto.jpg"))
//            val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.foto)
//            try {
//                // Konversi Bitmap ke Byte Array
//                val stream = ByteArrayOutputStream()
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
//                val byteArray = stream.toByteArray()
//
//                // Buat PdfWriter dan PdfDocument
////                val pdfWriter = PdfWriter(outputFilePath)
////                val pdfDocument = PdfDocument(pdfWriter)
////                val document = Document(pdfDocument)
//
//                // Tambahkan gambar ke PDF
////                val imageData: ImageData = ImageDataFactory.create(byteArray)
//                val image = Image(com.itextpdf.io.image.ImageDataFactory.create(byteArray))
////                val image = Image(imageData)
//                document.add(image)
//
//                // Tutup dokumen
////                document.close()
////                println("PDF berhasil dibuat di: $outputFilePath")
//            } catch (e: Exception) {
//                e.printStackTrace()
//                println("Gagal membuat PDF: ${e.message}")
//            }
            //tambah

            document.add(title)

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Add table headers
            val table = PdfPTable(3) // Assuming 3 columns: ID, Name, Email
            table.addCell("ID")
            table.addCell("Name")
            table.addCell("Age")

            // Add data from the SQLite database
            for (item in data) {
                table.addCell(item.id.toString())
                table.addCell(item.name)
                table.addCell(item.age)
            }

            // Add the table to the document
            document.add(table)

            // Close the document
            document.close()

            // Notify the user that the PDF has been generated
            Toast.makeText(context, "PDF Created Successfully $filePath", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

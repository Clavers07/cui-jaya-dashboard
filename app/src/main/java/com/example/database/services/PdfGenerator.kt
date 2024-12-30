package com.example.database.services

import android.content.Context
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.database.Formatter
import com.example.database.model.Bahan
import com.example.database.model.DataClass
import com.example.database.model.Pemasukan
import com.example.database.model.Sumber
import com.example.database.model.SumberJoinTotal
import com.itextpdf.text.BaseColor
import com.itextpdf.text.Document
import com.itextpdf.text.Chunk
import com.itextpdf.text.Font
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.Paragraph
import com.itextpdf.text.Element
import com.itextpdf.text.Element.ALIGN_MIDDLE
import com.itextpdf.text.Phrase
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import java.io.File
import java.io.FileOutputStream
//import com.itextpdf.text.

class PdfGenerator {

    // Prototype
    fun generatePdfFromData(context: Context, data: List<DataClass>, fileName: String = "database_data") {
        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PENJUALAN", titleFont)
            title.alignment = Element.ALIGN_CENTER

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
    // unused
    fun generatePemasukan(context: Context, data: List<DataClass>, date: String, fileName: String = "database_data") {

        try {
            val document = Document()
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName-$date.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PENJUALAN", titleFont)
            title.alignment = Element.ALIGN_CENTER

            document.add(title)

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Add table headers
            val table = PdfPTable(3)
            table.widthPercentage = 75F // Assuming 3 columns: ID, Name, Email
            table.addCell("ID")
            table.addCell("Name")
            table.addCell("Age")

            //set lebar max
            table.widthPercentage = 35F

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
            Toast.makeText(context, "PDF Created Successfully $filePath", Toast.LENGTH_SHORT)
                .show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error creating PDF: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }
    //unused
    fun customPdf(context: Context, data: List<DataClass>, date: String, fileName: String = "database_data") {

        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName-$date.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PENJUALAN", titleFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Add table headers
            val table = PdfPTable(3)

            table.defaultCell

            table.widthPercentage = 100F // Assuming 3 columns: ID, Name, Email
            table.addCell("ID")
            table.addCell("Name")
            table.addCell("Age")

            //set lebar max
            table.widthPercentage = 35F

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
            Toast.makeText(context, "Pdf dibuat di $filePath", Toast.LENGTH_SHORT)
                .show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error membuat PDF: ${e.message}", Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun pemasukanPdf(context: Context, data: List<Pemasukan>, date: String, fileName: String = "database_data") {

        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName-$date.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PEMASUKAN\nCUY JAYA", titleFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)

            var sum: Int = 0

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Define table columns
            val table = PdfPTable(4) // 4 columns: No, Nama, Nilai Pemasukan, Tanggal
            table.widthPercentage = 100F // Make the table width 100% of the page

            // Pembagian lebar kolom
            table.setWidths(floatArrayOf(10F, 25F, 25F, 40F))

            // Define a font for headers
            val headerFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.BLACK)

            // Set column headers with center alignment and bold font
            val noHeader = PdfPCell(Phrase("No", headerFont))
            noHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(noHeader)

            val namaHeader = PdfPCell(Phrase("Sumber", headerFont))
            namaHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(namaHeader)

            val nilaiPemasukanHeader = PdfPCell(Phrase("Nilai Pemasukan", headerFont))
            nilaiPemasukanHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(nilaiPemasukanHeader)

            val tanggalHeader = PdfPCell(Phrase("Tanggal", headerFont))
            tanggalHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(tanggalHeader)

            // Add data from the list
            var index = 1
            for (item in data) {
//                // Add each row, center aligned
//                table.addCell(index++.toString())  // No
//                table.addCell(item.nama)         // Nama
//                table.addCell(item.nilai.toString())  // Nilai Pemasukan
//                table.addCell(item.tanggal)      // Tanggal
                sum += item.nilai

                val red = if (index % 2 == 0) 225 else 217
                val green = if (index % 2 == 0) 225 else 225
                val blue = if (index % 2 == 0) 225 else 242

                val noCell = PdfPCell(Phrase(index.toString()))
                noCell.horizontalAlignment = Element.ALIGN_CENTER
                noCell.verticalAlignment = Element.ALIGN_MIDDLE
                noCell.backgroundColor = BaseColor(red, green, blue) // Set background color
                table.addCell(noCell)

                val namaCell = PdfPCell(Phrase(item.nama))
                namaCell.horizontalAlignment = Element.ALIGN_LEFT
                namaCell.verticalAlignment = Element.ALIGN_MIDDLE
                namaCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(namaCell)

                val pemasukanCell = PdfPCell(Phrase(Formatter.toCurrency(item.nilai)))
                pemasukanCell.horizontalAlignment = Element.ALIGN_RIGHT
                pemasukanCell.verticalAlignment = Element.ALIGN_MIDDLE
                pemasukanCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(pemasukanCell)

                val tanggalCell = PdfPCell(Phrase(item.tanggal))
                tanggalCell.horizontalAlignment = Element.ALIGN_CENTER
                tanggalCell.verticalAlignment = Element.ALIGN_MIDDLE
                tanggalCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(tanggalCell)

                index++
            }

            val sumFont = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.ITALIC, BaseColor.BLACK)
            val sumLine = Paragraph("Total: ${Formatter.toCurrency(sum)}", titleFont)
            title.alignment = Element.ALIGN_LEFT
            document.add(sumLine)
            document.add(Chunk.NEWLINE)

            // Add the table to the document
            document.add(table)

            // Close the document
            document.close()

            // Notify the user that the PDF has been generated
            Toast.makeText(context, "PDF dibuat di: $filePath", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membuat PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun bahanPdf(context: Context, data: List<Bahan>, date: String, fileName: String = "database_data") {

        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName-$date.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA PENGELUARAN\nCUY JAYA", titleFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)

            var sum: Int = 0

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Define table columns
            val table = PdfPTable(4) // 4 columns: No, Nama, Nilai Pemasukan, Tanggal
            table.widthPercentage = 100F // Make the table width 100% of the page

            // Pembagian lebar kolom
            table.setWidths(floatArrayOf(10F, 25F, 25F, 40F))

            // Define a font for headers
            val headerFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.BLACK)

            // Set column headers with center alignment and bold font
            val noHeader = PdfPCell(Phrase("No", headerFont))
            noHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(noHeader)

            val namaHeader = PdfPCell(Phrase("Pengeluaran", headerFont))
            namaHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(namaHeader)

            val nilaiPemasukanHeader = PdfPCell(Phrase("Nilai", headerFont))
            nilaiPemasukanHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(nilaiPemasukanHeader)

            val tanggalHeader = PdfPCell(Phrase("Tanggal", headerFont))
            tanggalHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(tanggalHeader)

            // Add data from the list
            var index = 1
            for (item in data) {
//                // Add each row, center aligned
//                table.addCell(index++.toString())  // No
//                table.addCell(item.nama)         // Nama
//                table.addCell(item.nilai.toString())  // Nilai Pemasukan
//                table.addCell(item.tanggal)      // Tanggal
                sum += item.harga

                val red = if (index % 2 == 0) 225 else 217
                val green = if (index % 2 == 0) 225 else 225
                val blue = if (index % 2 == 0) 225 else 242

                val noCell = PdfPCell(Phrase(index.toString()))
                noCell.horizontalAlignment = Element.ALIGN_CENTER
                noCell.verticalAlignment = Element.ALIGN_MIDDLE
                noCell.backgroundColor = BaseColor(red, green, blue) // Set background color
                table.addCell(noCell)

                val namaCell = PdfPCell(Phrase(item.name))
                namaCell.horizontalAlignment = Element.ALIGN_LEFT
                namaCell.verticalAlignment = Element.ALIGN_MIDDLE
                namaCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(namaCell)

                val pemasukanCell = PdfPCell(Phrase(Formatter.toCurrency(item.harga)))
                pemasukanCell.horizontalAlignment = Element.ALIGN_RIGHT
                pemasukanCell.verticalAlignment = Element.ALIGN_MIDDLE
                pemasukanCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(pemasukanCell)

                val tanggalCell = PdfPCell(Phrase(item.tanggal))
                tanggalCell.horizontalAlignment = Element.ALIGN_CENTER
                tanggalCell.verticalAlignment = Element.ALIGN_MIDDLE
                tanggalCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(tanggalCell)

                index++
            }

            val sumFont = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.ITALIC, BaseColor.BLACK)
            val sumLine = Paragraph("Total: ${Formatter.toCurrency(sum)}", titleFont)
            title.alignment = Element.ALIGN_LEFT
            document.add(sumLine)
            document.add(Chunk.NEWLINE)

            // Add the table to the document
            document.add(table)

            // Close the document
            document.close()

            // Notify the user that the PDF has been generated
            Toast.makeText(context, "PDF dibuat di: $filePath", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membuat PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    fun sumberPdf(context: Context, data: List<SumberJoinTotal>, date: String, fileName: String = "database_data") {

        try {
            val document = Document()
            val downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePath = File(downloadDir, "$fileName-$date.pdf")

            // Create a file output stream
            val outputStream = FileOutputStream(filePath)
            PdfWriter.getInstance(document, outputStream)

            // Open document for writing
            document.open()

            // Add a title
            val titleFont = Font(Font.FontFamily.HELVETICA, 18f, Font.BOLD, BaseColor.BLACK)
            val title = Paragraph("DATA  SUMBER PEMASUKAN\nCUY JAYA", titleFont)
            title.alignment = Element.ALIGN_CENTER
            document.add(title)

            var sum: Int = 0

            // Add a new line
            document.add(Chunk.NEWLINE)

            // Define table columns
            val table = PdfPTable(5) // 4 columns: No, Nama, Nilai Pemasukan, Tanggal
            table.widthPercentage = 100F // Make the table width 100% of the page

            // Pembagian lebar kolom
            table.setWidths(floatArrayOf(5F, 25F, 26F, 20F, 24F))

            // Define a font for headers
            val headerFont = Font(Font.FontFamily.HELVETICA, 12f, Font.BOLD, BaseColor.BLACK)

            // Set column headers with center alignment and bold font
            val noHeader = PdfPCell(Phrase("No", headerFont))
            noHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(noHeader)

            val namaHeader = PdfPCell(Phrase("Sumber Pemasukan", headerFont))
            namaHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(namaHeader)

            val alamatHeader = PdfPCell(Phrase("Alamat", headerFont))
            alamatHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(alamatHeader)

            val nilaiPemasukanHeader = PdfPCell(Phrase("Nilai", headerFont))
            nilaiPemasukanHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(nilaiPemasukanHeader)

            val tanggalHeader = PdfPCell(Phrase("Tanggal", headerFont))
            tanggalHeader.horizontalAlignment = Element.ALIGN_CENTER
            table.addCell(tanggalHeader)

            // Add data from the list
            var index = 1
            for (item in data) {

                sum += item.total

                val red = if (index % 2 == 0) 225 else 217
                val green = if (index % 2 == 0) 225 else 225
                val blue = if (index % 2 == 0) 225 else 242

                //no, nama, alamat, total, tanggal

                val noCell = PdfPCell(Phrase(index.toString()))
                noCell.horizontalAlignment = Element.ALIGN_CENTER
                noCell.verticalAlignment = Element.ALIGN_MIDDLE
                noCell.backgroundColor = BaseColor(red, green, blue) // Set background color
                table.addCell(noCell)

                val namaCell = PdfPCell(Phrase(item.nama))
                namaCell.horizontalAlignment = Element.ALIGN_LEFT
                namaCell.verticalAlignment = Element.ALIGN_MIDDLE
                namaCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(namaCell)

                val alamatCell = PdfPCell(Phrase(item.alamat))
                namaCell.horizontalAlignment = Element.ALIGN_LEFT
                namaCell.verticalAlignment = Element.ALIGN_MIDDLE
                namaCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(alamatCell)

                val pemasukanCell = PdfPCell(Phrase(Formatter.toCurrency(item.total)))
                pemasukanCell.horizontalAlignment = Element.ALIGN_RIGHT
                pemasukanCell.verticalAlignment = Element.ALIGN_MIDDLE
                pemasukanCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(pemasukanCell)

                val tanggalCell = PdfPCell(Phrase(item.tanggal))
                tanggalCell.horizontalAlignment = Element.ALIGN_CENTER
                tanggalCell.verticalAlignment = Element.ALIGN_MIDDLE
                tanggalCell.backgroundColor = BaseColor(red, green, blue)
                table.addCell(tanggalCell)

                index++
            }

            val sumFont = Font(Font.FontFamily.TIMES_ROMAN, 14f, Font.ITALIC, BaseColor.BLACK)
            val sumLine = Paragraph("Total: ${Formatter.toCurrency(sum)}", titleFont)
            title.alignment = Element.ALIGN_LEFT
            document.add(sumLine)
            document.add(Chunk.NEWLINE)

            // Add the table to the document
            document.add(table)

            // Close the document
            document.close()

            // Notify the user that the PDF has been generated
            Toast.makeText(context, "PDF dibuat di: $filePath", Toast.LENGTH_SHORT).show()
            Log.d("PDF", "PDF Created Successfully $filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Gagal membuat PDF: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

}

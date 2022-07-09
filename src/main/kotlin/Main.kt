import com.spire.pdf.FileFormat
import com.spire.pdf.PdfDocument
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDResources
import org.apache.pdfbox.pdmodel.graphics.form.PDFormXObject
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import org.apache.pdfbox.rendering.PDFRenderer
import java.awt.image.RenderedImage
import java.io.File
import java.io.IOException
import javax.imageio.ImageIO


fun main() {
    println("Enter pdf path:")
    var file = readln()
//    readFileLineByLineUsingForEachLine(file)
    wholePageSolution(file);
}

fun wholePageSolution(file: String) {
    var doc = PdfDocument()
    doc.loadFromFile(file)
    doc.saveToFile("layout.svg", FileFormat.SVG)
    doc.close()

}

fun readFileLineByLineUsingForEachLine(fileName: String) {

    var pdfDocument = PDDocument.load(File(fileName))
    try {
        userInputSolution(pdfDocument, fileName)
    } catch (e: Exception) {
        println("Unable to read pdf ${e.message}")
    } finally {
        pdfDocument.close()
    }
}

private fun userInputSolution (pdfDocument: PDDocument, fileName: String) {
    var pdfRenderer = PDFRenderer(pdfDocument)

    var img = pdfRenderer.renderImage(0)

    println("Read PDF file successfully - Width: ${img.width}   Height: ${img.height}")
    println("Enter image bounds")
    print("x = ")
    var x = readln().toInt()
    print("y = ")
    var y = readln().toInt()
    print("width = ")
    var width = readln().toInt()
    print("height = ")
    var height = readln().toInt()

    img = img.getSubimage(x, y, width, height)
    ImageIO.write(img, "JPEG", File("$fileName-image.png"))
}

@Throws(IOException::class)
private fun getImagesFromResources(resources: PDResources): List<RenderedImage>? {
    val images: MutableList<RenderedImage> = ArrayList()
    for (xObjectName in resources.xObjectNames) {
        val xObject = resources.getXObject(xObjectName)
        if (xObject is PDFormXObject) {
            images.addAll(getImagesFromResources(xObject.resources)!!)
        } else if (xObject is PDImageXObject) {
            images.add(xObject.image)
        }
    }
    return images
}

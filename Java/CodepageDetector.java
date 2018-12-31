
import cpdetector.io.ASCIIDetector;
import cpdetector.io.CodepageDetectorProxy;
import cpdetector.io.JChardetFacade;
import cpdetector.io.ParsingDetector;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


/**
 * 需要引入依赖
 * 用于探测文件编码，utf-8和gbk等常用编码判断
 * @author zewei.wang
 * @date 2018/9/5.
 */
public class CodepageDetector
{
    /** 探测器代理 **/
    private CodepageDetectorProxy detector;

    /**
     * 构造方法， detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
     *
     * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法
     *
     * 加进来，如ParsingDetector、 JChardetFacade、ASCIIDetector、UnicodeDetector。
     *
     * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
     *
     * 字符集编码。
     */
    public CodepageDetector()
    {
        detector = CodepageDetectorProxy.getInstance();
        // ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于
        // 指示是否显示探测过程的详细信息，为false不显示
        // 如果不希望判断xml的encoding，而是要判断该xml文件的编码，则可以注释掉
        detector.add(new ParsingDetector(false));
        // JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码测定。
        // 所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以再多加几个探测器，
        // 比如下面的ASCIIDetector、UnicodeDetector等。
        detector.add(JChardetFacade.getInstance());
        // ASCIIDetector用于ASCII编码测定
        detector.add(ASCIIDetector.getInstance());
        // UnicodeDetector用于Unicode家族编码的测定
//        detector.add(UnicodeDetector.getInstance());
    }

    /**
     * 探测
     *
     * @param file
     * @return charset
     * @throws MalformedURLException
     * @throws IOException
     */
    public Charset detector(File file) throws MalformedURLException, IOException
    {
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(fis,"GB2312");
        BufferedReader bufferedReader = new BufferedReader(reader);
        String firstLine = bufferedReader.readLine();
        System.out.println(Charset.forName("GB2312").newEncoder().canEncode(firstLine));
        return detector.detectCodepage(file.toURI().toURL());
    }

    public Charset detector(MultipartFile file) {
        Charset charset = null;
        File convFile = null;
        FileOutputStream fos=null;
        try {
            convFile = new File(file.getOriginalFilename());
            convFile.createNewFile();
            fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            charset = detector(convFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            assert fos != null;
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            convFile.delete();
        }
        return null == charset? Charset.defaultCharset():charset;
    }

    /**
     * 探测
     *
     * @param url
     * @return charset
     * @throws IOException
     */
    public Charset detector(URL url) throws IOException
    {
        return detector.detectCodepage(url);
    }

    /**
     * 探测
     *
     * @param inputStream
     * @param length
     *           长度应小于流的可用长度
     * @return charset
     * @throws IOException
     */
    public Charset detector(InputStream inputStream, int length)
            throws IOException
    {
        return detector.detectCodepage(inputStream, length);
    }

    public static void main(String[] args) throws IOException
    {
        System.out.println("\t不合格 | 开箱检验 | 无知情同意书， 检测项目不一致或未注明\\\n" +
                "\r\n" +
                "\n");
        File file = new File("C:\\Users\\le\\Desktop\\test.txt");
        CodepageDetector detector = new CodepageDetector();
        System.out.println(detector.detector(file));
    }
}

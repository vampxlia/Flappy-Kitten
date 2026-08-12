package GameLogic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.NoSuchElementException;

public class ScoreHandler {
    private final File inputFile;
    private final DocumentBuilderFactory dbFactory;
    private final DocumentBuilder dBuilder;
    private final Document doc;
    private final ArrayList<Integer> scores;
    int lastScore;
    int highScore;

    public ScoreHandler() {
        try {
            inputFile = new File("core/src/database/score.xml");

            dbFactory = DocumentBuilderFactory.newInstance();
            dBuilder = dbFactory.newDocumentBuilder();
            doc = dBuilder.parse(inputFile);

            scores = getScoreList(doc);
            try {
                this.highScore = Collections.max(this.scores);
            } catch (NoSuchElementException e) {
                this.highScore = 0;
            }

            //writeXML(writeScores(doc), Files.newOutputStream(inputFile.toPath()));

        } catch (ParserConfigurationException | IOException | SAXException |
                 XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public void addScores(int score) {
        this.lastScore = score;
    }

    public int getHighScore() {
        return this.highScore;
    }

    public void writeXML() {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            DOMSource source = new DOMSource(writeScores(this.doc));
            StreamResult result = new StreamResult(Files.newOutputStream(inputFile.toPath()));

            transformer.transform(source, result);
        } catch (IOException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Integer> getScoreList(Document doc) throws XPathExpressionException {
        ArrayList<Integer> scores = new ArrayList<>();
        NodeList nList = doc.getElementsByTagName("score");
        for (int i = 0; i < nList.getLength(); i++) scores.add(getScore(nList.item(i)));
        return scores;
    }

    private int getScore(Node nNode) {
        return Integer.parseInt(nNode.getTextContent());
    }

    private Document writeScores(Document doc) {
        Element Scores = (Element) doc.getElementsByTagName("Scores").item(0);
        Element score = doc.createElement("score");
        score.setTextContent(String.valueOf(this.lastScore));
        Scores.appendChild(score);
        return doc;

        //Document doc = dBuilder.newDocument();
        //Element DataBase = doc.createElement("DataBase");
        //Element Scores = doc.createElement("Scores");
        //for (Integer integer : scores) {
        //    Element score = doc.createElement("score");
        //    score.setTextContent(String.valueOf(integer));
        //    Scores.appendChild(score);
        //}
        //DataBase.appendChild(Scores);
        //doc.appendChild(DataBase);
        //return doc;
    }
}

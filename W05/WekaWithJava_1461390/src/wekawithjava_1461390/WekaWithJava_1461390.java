/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wekawithjava_1461390;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.trees.Id3;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author 1461390
 */
public class WekaWithJava_1461390 {

    public static void main(String[] args) {

        try {
            String filepath = "D:\\contact-lenses.arff";
            DataSource sr = new DataSource(filepath);
            Instances data = sr.getDataSet();

            if (data.classIndex() == -1) {
                data.setClassIndex(data.numAttributes() - 1);
            }

            NaiveBayesSimple nb = new NaiveBayesSimple();
            nb.buildClassifier(data);
            Evaluation evalNaive = new Evaluation(data);
            evalNaive.crossValidateModel(nb, data, 10, new Random(1), new Object[]{});

            System.out.println("NaiveBayesSimple: ");
//            System.out.print("Correctly Classified Instances: ");
//            System.out.println(evalNaive.correct());
            String strNaive = String.format("Correctly Classified Instances: %f %%", evalNaive.pctCorrect());
            System.out.println(strNaive);

            Id3 id = new Id3();
            id.buildClassifier(data);
            Evaluation evalId3 = new Evaluation(data);
            evalId3.crossValidateModel(id, data, 10, new Random(1), new Object[]{});
            System.out.println("Id3: ");
            String strId3 = String.format("Correctly Classified Instances: %f %%", evalId3.pctCorrect());
            System.out.println(strId3);
        } catch (Exception ex) {
            Logger.getLogger(WekaWithJava_1461390.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

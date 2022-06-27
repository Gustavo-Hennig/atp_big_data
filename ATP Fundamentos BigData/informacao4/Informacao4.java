/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pucpr.atp.gustavo;

import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 * @author hennig.gustavo
 */
public class Informacao4 {

    public static class MapInformacao extends Mapper<Object, Text, Text, IntWritable> {

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            try {
                
                String[] fields = value.toString().split(";");
                if (fields.length == 10) {
                   
                    Text keyField = new Text(fields[3]);
                    IntWritable valueMap = new IntWritable(1);

                    context.write(keyField, valueMap);

                }
            } catch (IOException | InterruptedException | NumberFormatException err) {
                System.out.println(err);
            }

        }

    }

    public static class ReduceInformacao extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int i = 0;

            for (IntWritable intvalues : values) {
                i += intvalues.get();
            }

            context.write(key, new IntWritable(i));

        }

    }

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {

        String pathIn = "/home/Disciplinas/FundamentosBigData/OperacoesComerciais/base_100_mil.csv";
        String pathOut = "/home2/ead2021/SEM1/hennig.gustavo/Desktop/atp/" + Informacao4.class.getSimpleName();

        if (args.length == 2) {
            pathIn = args[0];
            pathOut = args[1];
        }

        Configuration config = new Configuration();
        Job job = Job.getInstance(config, Informacao4.class.getSimpleName());

        job.setJarByClass(Informacao4.class);
        job.setMapperClass(MapInformacao.class);
        job.setReducerClass(ReduceInformacao.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(pathIn));
        FileOutputFormat.setOutputPath(job, new Path(pathOut));

        job.waitForCompletion(true);
    }
    
}

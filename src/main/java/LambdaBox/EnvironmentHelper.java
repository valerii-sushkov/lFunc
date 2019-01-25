package LambdaBox;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;
import dto.L_PROPERTY;
import helpersLocal.LConfig;
import helpersLocal.LogHandler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.StandardCopyOption;

public class EnvironmentHelper {
    private static LogHandler LOGGER = new LogHandler(EnvironmentHelper.class.getName());

    private EnvironmentHelper() {

    }

    public static void prepareEnvironment() {
        try {
            s3getToTmp("lambda_obj/" + L_PROPERTY.DRIVER_NAME.getValue(), L_PROPERTY.DRIVER_NAME.getValue());
            s3getToTmp("lambda_obj/" + L_PROPERTY.RUNNER_JAR.getValue(), L_PROPERTY.RUNNER_JAR.getValue());
        } catch (IOException e) {
            LOGGER.severe("Error getting file from s3!" + e.getMessage());
            e.printStackTrace();
        }
        Runtime r = Runtime.getRuntime();
        runShell(r, "chmod 777 " + L_PROPERTY.TEMP.getValue() + L_PROPERTY.DRIVER_NAME.getValue());
        runShell(r, "ls -l /tmp");
    }

    public static void runShell(Runtime r, String command) {
        try {
            Process proc  = r.exec(command);
            InputStream stdin = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);

            String line = "Command '" + command + "' output:";
            String subLine;
            while ( (subLine = br.readLine()) != null) {
                line = line + subLine + "; ";
            }
            line = line + proc.waitFor();
            LOGGER.info(line);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void s3getToTmp(final String fileName, final String newFinaleName) throws IOException {
        String clientRegion = "us-east-1";
        String bucketName = "smallbucket2000";
        String key = fileName;
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");
        S3Object fullObject = null;
        try {
            BasicAWSCredentials credentials = new
                    BasicAWSCredentials("AKIAJDVANBXUTYTMFRPQ",
                    "fDIjVozfesXi8msgCShmegDHDGFna5GhOM3UHljo");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            System.out.println("Downloading an object " + fileName);
            fullObject = s3Client.getObject(new GetObjectRequest(bucketName, key));
            displayTextInputStream(fullObject.getObjectContent(), newFinaleName);
        }
        catch(SdkClientException e) {
            LOGGER.severe("Error getting file from s3!" + e.getMessage());
            e.printStackTrace();
        }
        finally {
            // To ensure that the network connection doesn't remain open, close any open input streams.
            if(fullObject != null) {
                fullObject.close();
            }
        }
    }

    public static void s3getList(final String list) {
        String clientRegion = "us-east-1";
        String bucketName = "smallbucket2000";
        String key = "*** Object key ***";
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

        try {
            BasicAWSCredentials credentials = new
                    BasicAWSCredentials("AKIAJDVANBXUTYTMFRPQ",
                    "fDIjVozfesXi8msgCShmegDHDGFna5GhOM3UHljo");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            // Get an object and print its contents.
            System.out.println("Downloading an object");
            s3Client.listBuckets().stream().map(b -> b.getName()).forEach(b -> System.out.println("Buck:" + b));
            s3Client.listBuckets().forEach(b -> {
                System.out.println(">>Buck:" + b.getName());
                ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(b.getName()).withMaxKeys(6);
                ListObjectsV2Result result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
                }
            });
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
    }

    public static void s3put(final String list) {
        String clientRegion = "us-east-1";
        String bucketName = "smallbucket2000";
        String key = "*** Object key ***";
        System.setProperty(SDKGlobalConfiguration.DISABLE_CERT_CHECKING_SYSTEM_PROPERTY, "true");

        try {
            BasicAWSCredentials credentials = new
                    BasicAWSCredentials("AKIAJDVANBXUTYTMFRPQ",
                    "fDIjVozfesXi8msgCShmegDHDGFna5GhOM3UHljo");
            AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(clientRegion)
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .build();

            // Get an object and print its contents.
            System.out.println("Downloading an object");
            s3Client.listBuckets().stream().map(b -> b.getName()).forEach(b -> System.out.println("Buck:" + b));
            s3Client.listBuckets().forEach(b -> {
                System.out.println(">>Buck:" + b.getName());
                ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(b.getName()).withMaxKeys(6);
                ListObjectsV2Result result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
                    System.out.printf(" - %s (size: %d)\n", objectSummary.getKey(), objectSummary.getSize());
                }
            });
        }
        catch(AmazonServiceException e) {
            // The call was transmitted successfully, but Amazon S3 couldn't process
            // it, so it returned an error response.
            e.printStackTrace();
        }
    }

    private static void displayTextInputStream(InputStream input, final String fileNewName) throws IOException {
        File targetFile = new File(L_PROPERTY.TEMP.getValue() + fileNewName);

        java.nio.file.Files.copy(
                input,
                targetFile.toPath(),
                StandardCopyOption.REPLACE_EXISTING);

        input.close();
    }
}

package granch.sps;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import granch.sps.pars.DistanceMessage;
import granch.sps.pars.DistanceMessageParser;
import granch.sps.pars.MeasuredDistance;


public class UdpServer implements Runnable {

    // Серверный UDP-сокет запущен на этом порту

    private volatile boolean _running;
    public byte[] receivedData;
    byte[] receivingDataBuffer = new byte[1024];


    public interface DistanceMessageReceived {
        void OnDistanceReceived(Map<String, ConcurrentHashMap<String, MeasuredDistance>> distances);

    }

    private final DistanceMessageReceived callback;

    public UdpServer(DistanceMessageReceived callback) {
        this.callback = callback;
    }

    @SuppressLint("SetTextI18n")
    public void start_server() throws SocketException, UnknownHostException {


        if (_running)
            return;

        _running = true;

        // Создание нового экземпляря DatagramSocket, чтобы получать ответы от клиента
        DatagramSocket serverSocket = new DatagramSocket(CONST.SERVICE_PORT);
        DistanceMessageParser distanceMessageParser = new DistanceMessageParser();

        while (!Thread.currentThread().isInterrupted()) {

            /* Создание буферов для хранения отправляемых и получаемых данных.
Они временно хранят данные в случае задержек связи */

            /* Создание экземпляра UDP-пакета для хранения клиентских данных с использованием буфера для полученных данных */
            DatagramPacket inputPacket = new DatagramPacket(receivingDataBuffer,
                    receivingDataBuffer.length);
            //System.out.println("Waiting for a client to connect...");

            try {
                // Получение данных от клиента и сохранение их в inputPacket
                serverSocket.receive(inputPacket);
                receivedData = inputPacket.getData();
                long time = Instant.now().getEpochSecond();




                new Handler(Looper.getMainLooper()).post(() -> {
                    DistanceMessage data = distanceMessageParser.ParseMessage(receivedData, time);
                });


                if (Arrays.toString(receivedData).startsWith("stop")) {
                    stop_server();
                    System.out.println("Got stop request from the client");
                    break;
                }
                //System.out.println("Got some data from the client: " + data);
                callback.OnDistanceReceived(DistanceMessage.Distances);


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        System.out.println("Server is not running any longer");
        // Закрытие соединения сокетов
        serverSocket.close();
    }

    public void stop_server() {
        _running = false;
    }

    @Override
    public void run() {
        try {
            start_server();
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

    }
}








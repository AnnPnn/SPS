package granch.sps.pars;


import android.view.View;

import java.util.Arrays;

import granch.sps.MainActivity;

public class DistanceMessageParser  {

    public DistanceMessage ParseMessage(byte[] receivedData, long time) {
        MainActivity.ivError.setVisibility(View.GONE);
        int startIndex = 13 + (receivedData[12] * 8)+4;

        byte[] data;
        data = cleanArray(receivedData, startIndex);
        // System.out.println("Got some data from the client: " + Arrays.toString(data));


        //Проверка заголовка пакета
        if ((data[0] != -34) || (data[1] != -54))
        {
            System.out.println("Неверный формат пакета. Заголовок не найден.");
            MainActivity.ivError.setVisibility(View.VISIBLE);
        }
        // Проверяем, что указанная длина сообщения совпадает с фактической.
        int len = 0xFF * data[2] + data[3];

        if (len != data.length) {
            System.out.println("Неверная длина пакета.");
            System.out.println("len: " +len);
            System.out.println("len data: " +data.length);
            MainActivity.ivError.setVisibility(View.VISIBLE);
        }

        // TODO: тут надо проверить CRC

//        //Получение CRC от клиента
//        int crcStartIndex = 13 + (data[12] * 8);
//        byte[] crcReceivedBytes;
//
//            crcReceivedBytes = copyCRC(data, crcStartIndex);
//        System.out.println("dataCRC: " + Arrays.toString(crcReceivedBytes));
//        //long crcReceived = BitConverter.toInt(crcReceivedBytes, 0);
//        int crcReceived = BitConverter.toInt(crcReceivedBytes, 0);
//
//        //Подсчет CRC
//        CRC32 crc = new CRC32();
//        byte[] dataCRC = new byte[crcStartIndex];
//        System.arraycopy( data, 0, dataCRC, 0, crcStartIndex);
//        System.out.println("dataCRC: " + Arrays.toString(dataCRC));
//        crc.update(dataCRC);
//        int crc32Checksum = (int) crc.getValue();
//
//        //Сравнение полученных результатов
//        if(crc32Checksum==crcReceived){
//            System.out.println("CRC+: " +crc32Checksum +crcReceived);
//        }
//        else {
//            System.out.println("CRC-: " +crc32Checksum +"  "+crcReceived);
//        }


        // Парсим сообщение.
        try {
            return Parse(data, time);
        }
        catch (Exception ex) {
            System.out.println("Не удалось распарсить сообщение.");
            MainActivity.ivError.setVisibility(View.VISIBLE);
            return Parse(data, time);
        }
    }

    private static DistanceMessage Parse(byte[] data, long time){

        // Парсим основные параметры пакета.
        final byte[] macAddressSenderByte = {data[5], data[6], data[7]};
        String macAddressSender = Arrays.toString(macAddressSenderByte);
        int packageNumber = BitConverter.toInt(data, 8);
        final byte protocolVersion = data[4];

        DistanceMessage distanceMessage = new DistanceMessage();

        // Парсим расстояния от метки до анкеров.
        int distanceCount = data[12];
        for (int i = 0; i < distanceCount; i++) {
            int j = 13 + i * 8;
            String macAddressBS = BitConverter.toHexString(data, j);
            short decawave_m = BitConverter.toShort(data, j + 6);
            MeasuredDistance measuredDistance = new MeasuredDistance(decawave_m, time, packageNumber, protocolVersion);
            distanceMessage.map.put( macAddressBS, measuredDistance);
            DistanceMessage.Distances.put("00:11:21", distanceMessage.map);
        }
        return distanceMessage;
    }


//    private static String getCrc32(byte[] data, Checksum checksum) throws IOException {
//        try (
//
//                FileInputStream fis = new FileInputStream(Arrays.toString(data));
//                BufferedInputStream bis = new BufferedInputStream(fis);
//                CheckedInputStream cis = new CheckedInputStream(bis, checksum);
//        ) {
//            while (cis.read() >= 0) {
//
//            }
//            return Long.toHexString(cis.getChecksum().getValue());
//        }
//    }

    public static byte[] cleanArray(byte[] a, int startIndex) {
        byte[] r = new byte[startIndex];
        System.arraycopy(a, 0, r, 0, startIndex);
        return r;
    }

    public byte[] copyCRC (byte[] a, int startIndex) {
        byte[] r = new byte[4];
        System.arraycopy( a, startIndex, r, 0, r.length);
        return r;
    }



}


@RequestMapping(value = "/commons/getnewlike", method = RequestMethod.GET)
public @ResponseBody String getNewLike() {
    String apiKey = "mock_api_key";
    String accountKey = "mock_account_key";
    String mediaId = "our_media_id";
    String videoId = "our_video_id";

    try { // try to like video
        URL url = new URL("https://api.tikapi.io/user/like");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("X-API-KEY", apiKey);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("X-ACCOUNT-KEY", accountKey);
        conn.setRequestProperty("accept", "application/json");
        conn.setDoOutput(true);

        JSONObject requestBody = new JSONObject();
        requestBody.put("media_id", mediaId);
        log.info("request body: {}", requestBody);

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(requestBody.toString());
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
        }
        in.close();

        System.out.println("Response JSON: " + response.toString());

        try { // try to get video information
            URL url2 = new URL("https://api.tikapi.io/user/video?id=" + videoId);
            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
            conn2.setRequestMethod("GET");
            conn2.setRequestProperty("X-API-KEY", apiKey);
            conn2.setRequestProperty("X-ACCOUNT-KEY", accountKey);
            conn2.setRequestProperty("accept", "application/json");

            int responseCode2 = conn2.getResponseCode();
            System.out.println("Response Code: " + responseCode2);

            if (responseCode2 == HttpURLConnection.HTTP_OK) {
            BufferedReader in2 = new BufferedReader(new InputStreamReader(conn2.getInputStream()));
            String inputLine2;
            StringBuffer response2 = new StringBuffer();

            while ((inputLine2 = in2.readLine()) != null) {
            response2.append(inputLine2);
            }
            in2.close();

            JSONObject jsonResponse2 = new JSONObject(response2.toString());
            JSONObject itemInfo = jsonResponse2.getJSONObject("itemInfo");
            JSONObject itemStruct = itemInfo.getJSONObject("itemStruct");
            String diggCount = itemStruct.getJSONObject("stats").getString("diggCount"); // parsing the diggCount from the JSON response
            int totalNumLikes = Integer.parseInt(diggCount);
            meterRegistry.gauge("tiktok.totalNumLikes", totalNumLikes); // sending current like count to datadog

            } else {
            System.out.println("Error response: " + responseCode2);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("error, video information could not be retrieved");
            return "video information could not be retrieved, errored out: " + e;
        }
    } catch (Exception e) {
        e.printStackTrace();
        log.info("error, post has not been liked");
        return "post has not been liked, errored out: " + e;
    }
    log.info("logged successfully, post has been liked/vid info has been fetched");
    return "logged successfully, metric updated";
}
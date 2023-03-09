package edu.msu.singhk12.steampunked.cloud;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import edu.msu.singhk12.steampunked.Pipe;
import edu.msu.singhk12.steampunked.cloud.model.LoadPipe;
import edu.msu.singhk12.steampunked.cloud.model.RegisterResult;
import edu.msu.singhk12.steampunked.cloud.model.Room;
import edu.msu.singhk12.steampunked.cloud.model.User;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class Cloud {
    private static final String MAGIC = "NechAtHa6RuzeR8x";

    //private static final String BASE_URL = "https://webdev.cse.msu.edu/~singhk12/cse476/project2/";
    private static final String BASE_URL = "https://webdev.cse.msu.edu/~lizongy1/cse476/project2/";
    public static final String LOGIN_PATH = "steampunked-login.php";
    public static final String REGISTER_PATH = "steampunked-register.php";
    public static final String CREATE_PATH = "steampunked-createroom.php";
    public static final String JOIN_PATH = "steampunked-joinroom.php";
    public static final String SAVE_PATH = "steampunked-gridpipesave.php";
    public static final String LOAD_PATH = "steampunked-gridpipeload.php";
    public static final String ROOMSTATE_PATH = "steampunked-roomstate.php";
    public static final String ROOMINFO_PATH = "steampunked-roominfo.php";
    public static final String TURNINFO_PATH = "steampunked-turninfo.php";
    public static final String CHANGETURN_PATH = "steampunked-changeturn.php";
    public static final String DELETEPIPE_PATH = "steampunked-deletepipes.php";
    public static final String DELETEROOM_PATH = "steampunked-deleteroom.php";


    private static final String UTF8 = "UTF-8";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build();

    public User login(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<User> response = service.logIn(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return new User("no", "Server error " + response.code());
            }
            User user = (User) response.body();
            if (user.getStatus() == "no") {
                String string = "Failed to log in, msg is = " + user.getMessage();
                return new User("no", string);
            };
            return user;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return new User("no", "Exception");
        }
    }

    public boolean registerNew(String username, String password) {
        username = username.trim();
        if(username.length() == 0) {
            return false;
        }
        password = password.trim();
        if (password.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "user");

            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);
            xml.attribute(null, "magic", MAGIC);

            xml.endTag(null, "user");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        SteamPunkService service = retrofit.create(SteamPunkService.class);
        final String xmlStr = writer.toString();
        Log.i("11111", xmlStr);
        try {
            Response<RegisterResult> response = service.register(writer.toString()).execute();
            if (response.isSuccessful()) {
                RegisterResult result = response.body();
                if (result.getStatus() != null && result.getStatus().equals("yes")) {
                    return true;
                }
                Log.e("registerNew", "Failed to register, message = '" + result.getMessage() + "'");
                return false;
            }
            Log.e("registerNew", "Failed to register, message = '" + response.code() + "'");
            return false;
        } catch (IOException e) {
            Log.e("registerNew", "Exception occurred while trying to register user", e);
            return false;
        } catch (RuntimeException e) {	// to catch xml errors to help debug step 6
            Log.e("Register User", "Runtime Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean createRoom(String username, String size, String roomName, String password) {
        roomName = roomName.trim();
        if(roomName.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "user");
            xml.attribute(null, "magic", MAGIC);

            xml.startTag(null, "room");

            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);
            xml.attribute(null, "roomname", roomName);
            xml.attribute(null, "size", size);


            xml.endTag(null, "room");

            xml.endTag(null, "user");
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        SteamPunkService service = retrofit.create(SteamPunkService.class);
        final String xmlStr = writer.toString();
        try {
            Response<Room> response = service.createRoom(writer.toString()).execute();
            if (response.isSuccessful()) {
                Room result = response.body();
                if (result.getStatus() != null && result.getStatus().equals("yes")) {
                    return true;
                }
                Log.e("createRoom", "Failed to create room, message = '" + result.getMessage() + "'");
                return false;
            }
            Log.e("createRoom", "Failed to create room, message = '" + response.code() + "'");
            return false;
        } catch (IOException e) {
            Log.e("createRoom", "Exception occurred while trying to create room!", e);
            return false;
        } catch (RuntimeException e) {	// to catch xml errors to help debug step 6
            Log.e("createRoom", "Runtime Exception: " + e.getMessage());
            return false;
        }
    }

    public boolean joinRoom(String username, String roomName, String password) {
        roomName = roomName.trim();
        if(roomName.length() == 0) {
            return false;
        }

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "user");
            xml.attribute(null, "magic", MAGIC);

            xml.startTag(null, "room");
            xml.attribute(null, "username", username);
            xml.attribute(null, "password", password);
            xml.attribute(null, "roomname", roomName);
            xml.endTag(null, "room");

            xml.endTag(null, "user");
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        SteamPunkService service = retrofit.create(SteamPunkService.class);
        final String xmlStr = writer.toString();
        Log.i("a", xmlStr);
        try {
            Response<Room> response = service.joinRoom(writer.toString()).execute();
            if (response.isSuccessful()) {
                Room result = response.body();
                if (result.getStatus() != null && result.getStatus().equals("yes")) {
                    return true;
                }
                Log.e("joinRoom", "Failed to create room inside 'response.isSuccessful()', message = '" + result.getMessage() + "'");
                return false;
            }
            Log.e("joinRoom", "Failed to create room outside 'response.isSuccessful()', message = '" + response.code() + "'");
            return false;
        } catch (IOException e) {
            Log.e("joinRoom", "Exception occurred while trying to create room!", e);
            return false;
        } catch (RuntimeException e) {	// to catch xml errors to help debug step 6
            Log.e("joinRoom", "Runtime Exception: " + e.getMessage());
            return false;
        }
    }

    public User checkRoom(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<User> response = service.roomState(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return new User("no", "Exception");
            }
            User user = (User) response.body();
            if (user.getStatus() == "no") {
                String string = "Failed to log in, msg is = " + user.getMessage();
                return new User("no", "Exception");
            };
            Log.i("aaaa", user.getStatus());
            Log.i("bbbb", user.getMessage());
            return user;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return new User("no", "Exception");
        }
    }

    public String getRoomInfo(String username, String password, String index) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<ResponseBody> response = service.roomInfo(username, MAGIC, password, index).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return "fail";
            }
            String object = (String) response.body().string();
//            if (user.getStatus() == "no") {
//                String string = "Failed to log in, msg is = " + user.getMessage();
//                return new User("no", "Exception2");
//            };
//            Log.i("aaaa", user.getStatus());
//            Log.i("bbbb", user.getMessage());
            return object;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return "fail2";
        }
    }

    public String changeTurn(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<ResponseBody> response = service.changeTurn(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return "fail";
            }
            String object = (String) response.body().string();
            return object;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return "fail2";
        }
    }

    public boolean deletePipe(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<User> response = service.deletePipe(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return false;
            }
            User user = (User) response.body();
            if (user.getStatus() == "no") {
                String string = "Failed to log in, msg is = " + user.getMessage();
                return false;
            };
            return true;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return false;
        }
    }

    public boolean deleteRoom(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<User> response = service.deleteRoom(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return false;
            }
            User user = (User) response.body();
            if (user.getStatus() == "no") {
                String string = "Failed to log in, msg is = " + user.getMessage();
                return false;
            };
            return true;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return false;
        }
    }

    public String getTurnInfo(String username, String password) {
        // Create a service for the API call
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            // Attempt to log in on the server
            Response<ResponseBody> response = service.turnInfo(username, MAGIC, password).execute();
            // If the server could not be reached, return null
            if (!response.isSuccessful()) {
                return "fail";
            }
            String object = (String) response.body().string();
            return object;
        } catch(Exception e) {
            // There was another problem with processing the data from the database, return null
            Log.e("CatalogAdapter", "Something went wrong when loading the catalog", e);
            return "fail2";
        }
    }

    /**
     * Save a pipeArea to the cloud.
     * This should be run in a thread.
     * @return true if successful
     */
    public boolean saveGridPipeToCloud(String username, String password, Pipe pipe) throws IOException {

        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();

        try {
            xml.setOutput(writer);

            xml.startDocument("UTF-8", true);

            xml.startTag(null, "user");
            xml.attribute(null, "magic", MAGIC);

            xml.startTag(null, "steampunkedgridpipe");

            xml.attribute(null, "user", username);
            xml.attribute(null, "pw", password);
            xml.attribute(null, "connect0", pipe.getConnectxml(0));
            xml.attribute(null, "connect1", pipe.getConnectxml(1));
            xml.attribute(null, "connect2", pipe.getConnectxml(2));
            xml.attribute(null, "connect3", pipe.getConnectxml(3));
            xml.attribute(null, "originconnect0", pipe.getOriginalConnectxml(0));
            xml.attribute(null, "originconnect1", pipe.getOriginalConnectxml(1));
            xml.attribute(null, "originconnect2", pipe.getOriginalConnectxml(2));
            xml.attribute(null, "originconnect3", pipe.getOriginalConnectxml(3));

            xml.attribute(null, "x", String.valueOf(pipe.getX()));
            xml.attribute(null, "y", String.valueOf(pipe.getY()));

            xml.attribute(null, "id", String.valueOf(pipe.getId()));
            xml.attribute(null, "rotation", String.valueOf(pipe.getRotateAngle()));

            xml.endTag(null, "steampunkedgridpipe");

            xml.endTag(null, "user");

            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }

        SteamPunkService service = retrofit.create(SteamPunkService.class);
        Log.i("qqqqqqqqqqqqqqqqqqqqqq", writer.toString());
        try {
            Response<User> response = service.savePipe(writer.toString()).execute();
            if (response.isSuccessful()) {
                User result = response.body();
                if (result != null && result.getStatus().equals("yes")) {
                    return true;
                }
                Log.e("SaveToCloud", "Failed to save, message = '" + result.getMessage() + "'");
                return false;
            }
            Log.e("SaveToCloud", "Failed to save, message = '" + response.code() + "'");
            return false;
        } catch (IOException e) {
            Log.e("SaveToCloud", "Exception occurred while trying to save pipeArea!", e);
            return false;
        } catch (RuntimeException e) {    // to catch xml errors to help debug step 6
            Log.e("SaveToCloud", "Runtime Exception: " + e.getMessage());
            return false;
        }
    }

    public List<edu.msu.singhk12.steampunked.cloud.model.Pipe> loadPipe(String username, String password) {
        SteamPunkService service = retrofit.create(SteamPunkService.class);
        try {
            Response<LoadPipe> response = service.loadPipe(username, MAGIC, password).execute();

            // check if request failed
            if (!response.isSuccessful()) {
                Log.e("OpenFromCloud", "Failed to load hat, response code is = " + response.code());
                return null;
            }

            LoadPipe result = response.body();
            if (result.getStatus().equals("yes")) {
                return result.getItems();
            }

            Log.e("OpenFromCloud", "Failed to load hat, message is = '" + result.getMessage() + "'");
            return null;
        } catch (IOException | RuntimeException e) {
            Log.e("OpenFromCloud", "Exception occurred while loading hat!", e);
            return null;
        }
    }
}

/// create SavetoCloud()
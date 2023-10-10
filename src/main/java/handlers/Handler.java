package handlers;

import request.Request;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.List;

public interface Handler {
    void handle(Request request, BufferedOutputStream responseStream) throws IOException;
}

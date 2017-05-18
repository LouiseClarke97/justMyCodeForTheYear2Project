package controllers;

import controllers.security.AuthAdmin;
import controllers.security.Secured;
import play.mvc.*;
import play.data.*;
import play.db.ebean.Transactional;
import play.api.Environment;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import views.html.*;


import models.*;
import models.users.*;


public class HomeController extends Controller {

    public Result charts(Long cat) {

        List<Chart> chartList = Chart.findAll();

        if (cat == 0){
            chartList = Chart.findAll();
        }
        
        return ok(chart.render(chartList, getUserFromSession()));
	}
}
package controllers;

import controllers.security.AuthAdmin;
import controllers.security.Secured;
import play.data.Form;
import play.data.FormFactory;
import play.db.ebean.Transactional;
import play.mvc.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import play.api.Environment;

import views.html.admin.*;
import models.*;
import models.users.User;

import play.mvc.Http.*;
import play.mvc.Http.MultipartFormData.FilePart;
import java.io.File;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

public class AdminController extends Controller {

    public Result charts(Long cat) {

        List<Chart> chartList = new ArrayList<Chart>();

        if (cat == 0) {
            chartList = Chart.findAll();

        return ok(Chart.render(chartList, getUserFromSession(), env));
    }

    public Result addChart() {

        Form<Chart> addChartForm = formFactory.form(Chart.class);

        return ok(Chart.render(chartList, getUserFromSession(), env));
    }
    }

    @Transactional
    public Result addChartSubmit() {

        String saveImageMsg;
        Form<Chart> newChartForm = formFactory.form(Chart.class).bindFromRequest();

        if(newChartForm.hasErrors()) {
            return badRequest(addChart.render(newChartForm, getUserFromSession(), env));
        }

        Chart c = newChartForm.get();

        if (c.getId() == null) {
            c.save();
        }
        else if (c.getId() != null) {
            c.update();
        }

        MultipartFormData data = request().body().asMultipartFormData();
        FilePart image = data.getFile("upload");

        saveImageMsg = saveFile(c.getId(), image);

        flash("success", "Chart " + c.getName() + " has been created/ updated " + saveImageMsg);

        return redirect(routes.AdminController.Chart(0));
    }

    @Transactional
    public Result updateChart(Long id) {

        Chart c;
        Form<Chart> chartForm;

        try {
            c = Chart.find.byId(id);

            chartForm = formFactory.form(Chart.class).fill(c);

            } catch (Exception ex) {
                return badRequest("error");
        }
        return ok(addChart.render(chartForm, getUserFromSession(), env));
    }

    @Transactional
    public Result deleteChart(Long id) {

        Chart.find.ref(id).delete();
        flash("success", "Chart has been deleted");

        return redirect(routes.AdminController.Chart(0));
    }

    public String saveFile(Long id, FilePart<File> image) {
        if (image != null) {
            
            String mimeType = image.getContentType();
            
            if (mimeType.startsWith("image/")) {
                
                File file = image.getFile();
                
                ConvertCmd cmd = new ConvertCmd();
                
                IMOperation op = new IMOperation();
                
                op.addImage(file.getAbsolutePath());
                
                op.resize(300,200);
                
                op.addImage("public/images/productImages/" + id + ".jpg");
                
                IMOperation thumb = new IMOperation();
                
                thumb.addImage(file.getAbsolutePath());
                thumb.thumbnail(60);
                
                thumb.addImage("public/images/productImages/thumbnails/" + id + ".jpg");
                
                try{
                    cmd.run(op);
                    cmd.run(thumb);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                return " and image saved";
            }
        }
        return "image file missing";
    }
}
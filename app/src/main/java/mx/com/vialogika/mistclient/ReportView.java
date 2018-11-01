package mx.com.vialogika.mistclient;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class ReportView extends AppCompatActivity {

    private Reporte report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        getReport();
        setupActionBar();
    }

    private void getReport(){
        Intent intent  = getIntent();
        if(intent.hasExtra("Reporte")){
            report = (Reporte) intent.getSerializableExtra("Reporte");
            Toast.makeText(this,"Reporte conseguido con exito id : " + report.getReportId(),Toast.LENGTH_SHORT).show();
        }else{
            finish();
        }
    }

    private void setupActionBar(){
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(report.getReportTitle());
    }
}

package com.example.midwivesapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import com.example.midwivesapp.databinding.ActivityChartBinding
import com.google.firebase.auth.FirebaseAuth

class Chart : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding
    private lateinit var user:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityChartBinding.inflate(layoutInflater)
        user = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webView.webChromeClient = WebChromeClient()
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE

        // Load Google Charts HTML code
        val html = """
            <html>
                <head>
                    <script type="text/javascript" src="https://www.gstatic.com/charts/loader.js"></script>
                    <script type="text/javascript">
                        google.charts.load('current', { 'packages': ['corechart'] });
                        google.charts.setOnLoadCallback(drawChart);
                        
                        function drawChart() {
                          var data = google.visualization.arrayToDataTable([
                            ['Date', 'BMI'],
                            ['2022.05.05', 18],
                            ['2022.06.05', 22],
                            ['2022.07.05', 20],
                            ['2022.08.05', 15]
                          ]);
                        
                          var options = {
                            title: 'BMI Chart',
                            curveType: 'none', // Set curveType to 'none' for straight lines
                            legend: { position: 'bottom' },
                            pointSize: 5, // Set pointSize to add markers to the plots
                            vAxis: { minValue: 0 } // Optionally set minValue for the vertical axis
                          };
                        
                          var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
                          chart.draw(data, options);
                        }
                    </script>
                </head>
                <body>
                    <div id="chart_div" style="width: 100%; height: 100%;"></div>
                </body>
            </html>
        """.trimIndent()

        binding.webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)

    }
}
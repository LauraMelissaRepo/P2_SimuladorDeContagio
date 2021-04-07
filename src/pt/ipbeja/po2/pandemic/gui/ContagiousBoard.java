package pt.ipbeja.po2.pandemic.gui;

/**
 * @author Laura Melissa Bernardo Correia, nº 17179
 * https://github.com/PO2-2019-2020/tp2-17179-po2
 */

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.ipbeja.po2.pandemic.model.Cell;
import pt.ipbeja.po2.pandemic.model.CellPosition;
import pt.ipbeja.po2.pandemic.model.View;
import pt.ipbeja.po2.pandemic.model.World;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ContagiousBoard extends VBox implements View {

    private static World world;
    private WorldBoard pane;
    private Label counterLabel;
    public static int LINES ;
    public static int COLS ;
    private TextField nLines;
    private TextField nCols;
    private TextField nHealthy;
    private TextField nSick;
    private TextField nImmune;
    private TextField minSickTime;
    private TextField maxSickTime;
    private TextField probContg;
    private TextField probImm;
    private HBox hbBoardSize = new HBox();
    private HBox hbHealthy = new HBox();
    private HBox hbImmune = new HBox();
    private HBox hbSick = new HBox();
    private HBox hbSickTime = new HBox();
    private HBox hbProbContg = new HBox();
    private HBox outMenu = new HBox();
    private Button stopButton = new Button("Stop");
    private Button startButton = new Button("Start");
    private HBox menuButtons = new HBox();
    private MenuButton saveMenuButton;
    private Label inputs1 = new Label("   OR Enter the parameters above: ");
    private MenuItem writeFile = new MenuItem("Save as..");
    private MenuItem readFile = new MenuItem("Open");
    private boolean isaFile;
    private ArrayList<CellPosition> sickPersons = new ArrayList<>();
    private ArrayList<CellPosition> healthyPersons = new ArrayList<>();
    private ArrayList<CellPosition> immunePersons = new ArrayList<>();
    private BarChart<Number, String> chart;
    private HBox paneGraph = new HBox();
    private boolean stopGraph;


    public ContagiousBoard() {
        helper1();

        Button startButton = new Button("Start");
        this.getChildren().addAll(this.outMenu, this.hbBoardSize, this.hbHealthy, this.hbImmune, this.hbSick, this.hbSickTime, this.hbProbContg, startButton);

        this.menuButtons.getChildren().addAll(this.stopButton, this.startButton, this.saveMenuButton);

        startButton.setOnMouseClicked((e) -> {
            if(this.isaFile){
                this.getChildren().clear();
                world = new World(this, healthyPersons, immunePersons, sickPersons, this.LINES, this.COLS);
                graph();
                this.pane = new WorldBoard(this.world, 10);
                helper2();
                startButton.setPrefWidth(this.pane.getPrefWidth());
                this.getChildren().removeAll(this.hbHealthy, this.hbBoardSize, this.hbImmune, this.hbSick, this.hbSickTime, this.hbProbContg, startButton);
                this.getChildren().addAll(this.menuButtons, this.counterLabel, this.paneGraph);
                this.getScene().getWindow().sizeToScene();
            }
            else if (verifyInputs(this.nLines, this.nCols, this.nHealthy, this.nSick, this.nImmune, this.minSickTime, this.maxSickTime, this.probContg, this.probImm)) {
                this.getChildren().clear();
                int pctg = Integer.parseInt(this.probContg.getText());
                int pimm = Integer.parseInt(this.probImm.getText());
                int lines = Integer.parseInt(this.nLines.getText());
                int cols = Integer.parseInt(this.nCols.getText());
                int healthy = Integer.parseInt(this.nHealthy.getText());
                int sick = Integer.parseInt(this.nSick.getText());
                int immune = Integer.parseInt(this.nImmune.getText());
                int min = Integer.parseInt(this.minSickTime.getText());
                int max = Integer.parseInt(this.maxSickTime.getText());
                this.LINES = lines;
                this.COLS = cols;
                world = new World(this, lines, cols, healthy, sick, immune, min, max, pctg, pimm);
                graph();
                this.pane = new WorldBoard(this.world, 10);
                helper2();
                startButton.setPrefWidth(this.pane.getPrefWidth());
                this.getChildren().removeAll(this.hbBoardSize, this.hbHealthy, this.hbImmune, this.hbSick, this.hbSickTime, this.hbProbContg, startButton);
                this.getChildren().addAll(this.menuButtons, this.counterLabel, this.paneGraph);
                this.getScene().getWindow().sizeToScene();
            }
        });
    }

    /**
     * Complementary method to the constructor
     */
    private void helper1() {
        this.openFileMenu();
        this.saveFileMenu();
        this.getInputs1();
        this.worldReader();
    }

    /**
     * Complementary method to the constructor
     */
    public void helper2() {
        this.paneGraph.getChildren().addAll(this.pane, this.chart);
        this.counterLabel = new Label(("0"));
        this.counterLabel.setPrefWidth(this.pane.getPrefWidth());
        world.start();
        this.startAndStopButtons();
        this.worldWriter();
    }

    /**
     * Menu to open a file
     */
    public void openFileMenu() {
        this.inputs1.setPrefSize(200, 20);
        MenuButton openMenuButton = new MenuButton("File", null, readFile);
        this.outMenu.getChildren().addAll(openMenuButton, this.inputs1);
    }

    /**
     * Menu to sabe a file
     */
    private void saveFileMenu() {
        this.saveMenuButton = new MenuButton("File", null, writeFile);
    }


    @Override
    public void populateWorld(CellPosition position) {
        pane.populateWorld(position);
    }

    /**
     * Method to start and stop the game when the user presses de buttons
     */
    private void startAndStopButtons(){
        this.stopButton.setOnMouseClicked(event -> {
            stopGraph = true;
            world.stopGame();
        });
        this.startButton.setOnMouseClicked(event -> {
            stopGraph = false;
            world.start();
        });
    }

    /**
     * Method to get the inputs
     */
    private void getInputs1() {
        getInputs2();

        Label labelMinSick = new Label("Minimum time to be sick:");
        this.minSickTime = new TextField();
        restrictInput(minSickTime);
        Label labelMaxSick = new Label("Maximum time to be sick:");
        this.maxSickTime = new TextField();
        restrictInput(this.maxSickTime);
        this.hbSickTime.getChildren().addAll(labelMinSick, this.minSickTime, labelMaxSick, this.maxSickTime);
        this.hbSickTime.setSpacing(10);

        Label labelProbContg = new Label("Probability of contagion:");
        this.probContg = new TextField();
        restrictInput(this.probContg);
        this.hbProbContg.setSpacing(10);

        Label labelProbImm = new Label("Probability of get immune:");
        this.probImm = new TextField();
        restrictInput(this.probImm);
        this.hbProbContg.getChildren().addAll(labelProbContg, this.probContg, labelProbImm, this.probImm);
    }

    private void getInputs2(){
        Label labelLines = new Label("Number of lines:");
        this.nLines = new TextField();
        restrictInput(this.nLines);

        Label labelCols = new Label("Number of columns:");
        this.nCols = new TextField();
        restrictInput(this.nCols);
        this.hbBoardSize.setSpacing(10);
        this.hbBoardSize.getChildren().addAll(labelLines, this.nLines, labelCols, this.nCols);


        Label labelHealthy = new Label("Number of healthy people:");
        this.nHealthy = new TextField();
        restrictInput(this.nHealthy);
        this.hbHealthy.getChildren().addAll(labelHealthy, this.nHealthy);
        this.hbHealthy.setSpacing(10);

        Label labelImmune = new Label("Number of immunity persons:");
        this.nImmune = new TextField();
        restrictInput(this.nImmune);
        this.hbImmune.getChildren().addAll(labelImmune, this.nImmune);
        this.hbImmune.setSpacing(10);

        Label labelSick = new Label("Number of sick persons:");
        this.nSick = new TextField();
        restrictInput(this.nSick);
        this.hbSick.getChildren().addAll(labelSick, this.nSick);
        this.hbSick.setSpacing(10);

    }


    private void restrictInput(TextField tf) {
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    /***
     * Method that verifies if the user inputs are correct
     * @param nlines
     * @param ncols
     * @param nHealthy
     * @param nSick
     * @param nImmune
     * @param minSickTime
     * @param maxSickTime
     * @param probContg
     * @param probImm
     * @return
     */
    private boolean verifyInputs(TextField nlines, TextField ncols, TextField nHealthy, TextField nSick, TextField nImmune, TextField minSickTime, TextField maxSickTime, TextField probContg, TextField probImm) {
        if (nlines.getText().trim().isEmpty() || nHealthy.getText().trim().isEmpty() ||
                nSick.getText().trim().isEmpty() || nImmune.getText().trim().isEmpty() ||
                minSickTime.getText().trim().isEmpty() || maxSickTime.getText().trim().isEmpty() ||
                probContg.getText().trim().isEmpty() || probImm.getText().trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Há campos vazios.");
            alert.show();
            return false;
        }

        int lines = Integer.parseInt(nlines.getText());
        int cols = Integer.parseInt(ncols.getText());
        int healthy = Integer.parseInt(nHealthy.getText());
        int sick = Integer.parseInt(nSick.getText());
        int immune = Integer.parseInt(nImmune.getText());
        int min = Integer.parseInt(minSickTime.getText());
        int max = Integer.parseInt(maxSickTime.getText());
        int pcntg = Integer.parseInt(probContg.getText());
        int pimm = Integer.parseInt(probImm.getText());


        int MAX_PERSONS = (lines * cols) / 2;

        if(lines < 10 || cols < 10){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The number of lines and columns are not supported.");
            alert.show();
            return false;
        }
        else if (healthy + sick + immune > MAX_PERSONS) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You have exceeded the maximum number of people.");
            alert.show();
            return false;
        } else if (min <= 0 || min >= max) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Time limits are not supported.");
            alert.show();
            return false;
        }
        else if(pcntg > 100 || pimm >
                100) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The odds that were entered are not compatible, please enter a value less than or equal to 100.");
            alert.show();
            return false;
        }
        return true;
    }

    /**
     * Method that calls other method when the button read file is clicked
     */
    private void worldReader() {
        this.readFile.setOnAction(event -> {
            reader();
        });
    }

    /**
     * Method that reads the file chosen by the user
     */
    private void reader() {
        int i = 0;
        FileChooser path = new FileChooser();
        File f = path.showOpenDialog(new Stage());
        String absolutePath = " ";
        StringBuilder read = new StringBuilder("");
        if (f != null) {
            absolutePath = f.getAbsolutePath();
        }
        try {
            BufferedReader buffReader = new BufferedReader(new FileReader(absolutePath));
            String line = buffReader.readLine();
            while (line != null) {
                System.out.printf("%s\n", line);
                if(i == 0) {
                    LINES = Integer.parseInt(line);
                }
                if(i == 1) {
                    COLS = Integer.parseInt(line);
                }
                read.append(line).append("\n");
                line = buffReader.readLine();
                i++;
            }
            saveValues(read.toString());
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo : %s.\n", e.getMessage());
        }
        System.out.println();
        this.isaFile = true;
    }


    /**
     * Method that stores the position values ​​in 3 arrays depending on the type of person
     * @param read
     */
    private void saveValues(String read) {
        String healthyCoords = read.substring(read.indexOf("Healthy") + 8, read.indexOf("Immune"));
        String immuneCoords = read.substring(read.indexOf("Immune") + 7, read.indexOf("Sick"));
        String sickCoords = read.substring(read.indexOf("Sick") + 5, read.length());

        if (!healthyCoords.equals("")) {
            for (String healthyPerson : healthyCoords.split("\n", 0)) {
                String[] healthyxy = healthyPerson.split(" ", 0);
                int hx = Integer.parseInt(healthyxy[0]);
                int hy = Integer.parseInt(healthyxy[1]);
                healthyPersons.add(new CellPosition(hx, hy));
            }
        }
        if (!immuneCoords.equals("")) {
            for (String immunePerson : immuneCoords.split("\n", 0)) {
                String[] immunexy = immunePerson.split(" ", 0);
                int ix = Integer.parseInt(immunexy[0]);
                int iy = Integer.parseInt(immunexy[1]);
                immunePersons.add(new CellPosition(ix, iy));

            }
        }
        if (!sickCoords.equals("")) {
            for (String sickPerson : sickCoords.split("\n", 0)) {
                String[] sickxy = sickPerson.split(" ", 0);
                int sx = Integer.parseInt(sickxy[0]);
                int sy = Integer.parseInt(sickxy[1]);
                sickPersons.add(new CellPosition(sx, sy));
            }
        }
        System.out.println(Arrays.toString(healthyPersons.toArray()));
        System.out.println(Arrays.toString(immunePersons.toArray()));
        System.out.println(Arrays.toString(sickPersons.toArray()));
    }

    /**
     * Method that calls other method when the button write file is clicked
     */
    private void worldWriter() {
        this.writeFile.setOnAction(event -> {
            try {
                writer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Method that writes the values into the txt file
     * @throws IOException
     */
    private static void writer() throws IOException {
        FileChooser path = new FileChooser();
        File f = path.showSaveDialog(new Stage());
        String absolutePath = " ";
        if (f != null) {
            absolutePath = f.getAbsolutePath() + ".txt";
        }
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(absolutePath));
        String line;
        System.out.println("");
        line = world.writeFile();
        buffWrite.append(line);
        buffWrite.close();
    }

    /**
     * Method that creates and updates chart values. This figure includes the number of people on the grid
     */
    public void graph() {
        String healthyPersones = "Healthy Persons";
        String sickPersons = "Sick Persons";
        String immunePersons = "Immunes Persons";
        NumberAxis xAxis = new NumberAxis();
        CategoryAxis yAxis = new CategoryAxis();

        chart = new BarChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);

        chart.setTitle("Amount of People");
        xAxis.setLabel("Amount");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Sick Persons");

        XYChart.Series sickPersonsSeries = new XYChart.Series();
        sickPersonsSeries.setName("Sick Persons");

        XYChart.Series healthyPersonsSeries = new XYChart.Series();
        healthyPersonsSeries.setName("Healthy Persons");

        XYChart.Series immunePersonsSeries = new XYChart.Series();
        immunePersonsSeries.setName("Immune Persons");

        chart.getData().addAll(healthyPersonsSeries, sickPersonsSeries, immunePersonsSeries);

        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() ->{
            Platform.runLater(()->{
                if(stopGraph){

                }else{
                    XYChart.Data healthyP = new XYChart.Data<>(world.numberOfHealthy, healthyPersones);
                    XYChart.Data sickP = new XYChart.Data<>(world.numberOfSick, sickPersons);
                    XYChart.Data immuneP = new XYChart.Data<>(world.numberOfImmune, immunePersons);

                    healthyPersonsSeries.getData().add(healthyP);
                    immunePersonsSeries.getData().add(sickP);
                    sickPersonsSeries.getData().add(immuneP);
                }

                Node n = chart.lookup(".chart-bar.series2");
                n.setStyle("-fx-bar-fill: red");

                n = chart.lookup(".chart-bar.series0");
                n.setStyle("-fx-bar-fill: green");

                n = chart.lookup(".chart-bar.series1");
                n.setStyle("-fx-bar-fill: blue");
            });
        },0, 1550, TimeUnit.MILLISECONDS);
    }


    @Override
    public void updatePosition(int oldLine, int oldCol, int xx, int yy, int i) {
        Platform.runLater(() -> {
            this.pane.updatePosition(oldLine, oldCol, xx, yy);
            System.out.println(i);
            this.counterLabel.setText("" + i);
        });
    }

    @Override
    public void setCells(Cell[][] cells) {
        Platform.runLater(() -> {
            pane.setCells(cells);
        });
    }
}
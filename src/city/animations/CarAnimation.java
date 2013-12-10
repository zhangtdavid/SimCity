package city.animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import city.Application;
import city.agents.interfaces.Car;
import city.animations.interfaces.AnimatedCar;
import city.bases.Animation;
import city.bases.interfaces.BuildingInterface;
import city.gui.CityRoad;
import city.gui.CityRoadIntersection;
import city.gui.exteriors.CityViewApt;

public class CarAnimation extends Animation implements AnimatedCar {

        // Data

        private Car car = null;

        private int xPos, yPos;
        private int xDestination, yDestination;

        private BuildingInterface currentBuilding;
        private BuildingInterface destinationBuilding = null;
        private CityRoad startingRoad = null;
        private CityRoad endRoad = null;
        private Rectangle rectangle;

        private boolean atDestinationRoad = false;
        private boolean atDestination = true;

        private static BufferedImage cityViewCarNorthImage = null;
        private static BufferedImage cityViewCarEastImage = null;
        private static BufferedImage cityViewCarSouthImage = null;
        private static BufferedImage cityViewCarWestImage = null;
        private BufferedImage imageToRender;

        public CarAnimation(Car c, BuildingInterface startingBuilding) {
                car = c;
                xDestination = xPos = startingBuilding.getCityViewBuilding().getX();
                yDestination = yPos = startingBuilding.getCityViewBuilding().getY();
                rectangle = new Rectangle(xPos, yPos, SIZE, SIZE);
                currentBuilding = startingBuilding;
                try {
                        if(cityViewCarNorthImage == null || cityViewCarEastImage == null || cityViewCarSouthImage == null || cityViewCarWestImage == null) {
                                cityViewCarNorthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewCarNorthImage.png"));
                                cityViewCarEastImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewCarEastImage.png"));
                                cityViewCarSouthImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewCarSouthImage.png"));
                                cityViewCarWestImage = ImageIO.read(CityViewApt.class.getResource("/icons/cityView/CityViewCarWestImage.png"));
                                imageToRender = cityViewCarNorthImage;
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        // Paint

        public void updatePosition() {
                // Getting on the first road
                if(startingRoad != null) {
                        if(startingRoad.setVehicle(this) == false) {
                                return;
                        }
                        if(startingRoad.isWalkerAt(xPos, yPos)) {
                                return;
                        }
                        if (xPos < startingRoad.getX() && !startingRoad.getHorizontal()) {
                                xPos++;
                                imageToRender = cityViewCarEastImage;
                        }
                        else if (xPos > startingRoad.getX() && !startingRoad.getHorizontal()) {
                                xPos--;
                                imageToRender = cityViewCarWestImage;
                        }
                        if (yPos < startingRoad.getY() && startingRoad.getHorizontal()) {
                                yPos++;
                                imageToRender = cityViewCarSouthImage;
                        }
                        else if (yPos > startingRoad.getY() && startingRoad.getHorizontal()) {
                                yPos--;
                                imageToRender = cityViewCarNorthImage;
                        }
                        if(xPos == startingRoad.getX() && yPos == startingRoad.getY())
                                startingRoad = null;
                }
                // Getting on the destination road
                if(atDestinationRoad == true) {
                        if(endRoad.isWalkerAt(xPos, yPos)) {
                                return;
                        }
                        if (xPos < xDestination) {
                                xPos++;
                                imageToRender = cityViewCarEastImage;
                        } else if (xPos > xDestination) {
                                xPos--;
                                imageToRender = cityViewCarWestImage;
                        }
                        if (yPos < yDestination) {
                                yPos++;
                                imageToRender = cityViewCarSouthImage;
                        } else if (yPos > yDestination) {
                                yPos--;
                                imageToRender = cityViewCarNorthImage;
                        }
                }
                if(xPos == xDestination && yPos == yDestination && atDestination == false) {
                        atDestination = true;
                        atDestinationRoad = false;
                        currentBuilding = destinationBuilding;
                        destinationBuilding = null;
                        this.setVisible(false);
                        car.msgAtDestination();
                }
        }

        public void draw(Graphics2D g) {
                if(isUgly) {
                        g.setColor(Color.PINK);
                        g.fillRect(xPos, yPos, SIZE, SIZE);
                        g.setColor(Color.red);
                        rectangle.setLocation(xPos, yPos);
                        if(car != null)
                                g.drawString(car.getClass().getSimpleName(), xPos, yPos);
                } else {
                        if(Application.trafficControl != null && startingRoad == null && atDestinationRoad == false) {
                                CityRoad currentRoad = Application.trafficControl.getRoadThatVehicleIsOn(this);
                                if(currentRoad.getClass() == CityRoadIntersection.class)
                                        currentRoad = ((CityRoadIntersection)currentRoad).getCurrentNextRoad();
                                if(currentRoad == null)
                                        imageToRender = cityViewCarEastImage;
                                else if(currentRoad.getXVelocity() > 0)
                                        imageToRender = cityViewCarEastImage;
                                else if(currentRoad.getXVelocity() < 0)
                                        imageToRender = cityViewCarWestImage;
                                else if(currentRoad.getYVelocity() < 0)
                                        imageToRender = cityViewCarNorthImage;
                                else if(currentRoad.getYVelocity() > 0)
                                        imageToRender = cityViewCarSouthImage;
                        }
                        g.drawImage(imageToRender, xPos, yPos, null);
                }
        }

        // Action

        public void goToDestination(BuildingInterface destination) {
                destinationBuilding = destination;
                startingRoad = Application.CityMap.findClosestRoad(currentBuilding);
                xDestination = destination.getCityViewBuilding().getX();
                yDestination = destination.getCityViewBuilding().getY();
                endRoad = Application.CityMap.findClosestRoad(destination);
                atDestination = false;
                atDestinationRoad = false;
                this.setVisible(true);
                this.car.print("Going to destination " + destination);
        }

        // Getters

        @Override
        public int getXPos() {
                return xPos;
        }

        @Override
        public int getYPos() {
                return yPos;
        }

        @Override
        public Car getCar() {
                return car;
        }

        @Override
        public BuildingInterface getDestinationBuilding() {
                return destinationBuilding;
        }

        @Override
        public CityRoad getEndRoad() {
                return endRoad;
        }

        @Override
        public boolean getAtDestinationRoad() {
                return atDestinationRoad;
        }

        @Override
        public CityRoad getStartingRoad() {
                return startingRoad;
        }

        // Setters

        @Override
        public void setXPos(int x) {
                xPos = x;
        }

        @Override
        public void setYPos(int y) {
                yPos = y;
        }

        @Override
        public void setDestinationBuilding(BuildingInterface destinationBuilding) {
                this.destinationBuilding = destinationBuilding;
        }

        @Override
        public void setEndRoad(CityRoad endRoad) {
                this.endRoad = endRoad;
        }

        @Override
        public void setAtDestinationRoad(boolean atDestinationRoad) {
                this.atDestinationRoad = atDestinationRoad;
        }

        @Override
        public void setStartingRoad(CityRoad startingRoad) {
                this.startingRoad = startingRoad;
        }

        // Utilities

        @Override
        public boolean contains(int x, int y) {
                return rectangle.contains(x, y);
        }
}
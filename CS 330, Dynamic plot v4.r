# Project:  CS 330
# Program:  Dynamic plot
# Purpose:  Plot a movement trajectory generated by Dynamic movement.
# Author:   Mikel D. Petty, Ph.D., 256-824-6140, pettym@uah.edu
# Created:  2020-1-30
# Modified: 2020-2-19

# Clear workspace.

rm(list = ls())

# Initialize file paths and names.

location        <- 1  # 1=OKT N353, 2=27CPD
work.paths      <- c("C:/Users/quanrocks11/Documents/CS 330 AI/Program 2/CS 330 20S, P2, Nguyen, Path following/")
work.path       <- work.paths[location]
trajectory.file <- paste(work.path, "test70.txt", sep="")

# Define needed support functions.

num.width <- function(x, left, right) { format(round(x, right),  nsmall=right, width=(left + right + ifelse(right > 0, 1, 0))) }

distance.point.point <- function(A, B) {
  return(sqrt((B[1] - A[1])^2 + (B[2] - A[2])^2))
}

path.prep <- function(path.id, path.x, path.y) {
  path.segments <- length(path.x) - 1
  path.distance <- rep(0, path.segments + 1)
  for (i in 2:(path.segments + 1)) {
    path.distance[i] <- path.distance[i - 1] + distance.point.point(c(path.x[i - 1], path.y[i - 1]), c(path.x[i], path.y[i]))
  }
  path.param <- rep(0, path.segments + 1)
  for (i in 2:(path.segments + 1)) {
    path.param[i] <- path.distance[i] / max(path.distance)
  }
  return(list(id=path.id, x=path.x, y=path.y, distance=path.distance, param=path.param, segments=path.segments))
}

# Set plot control parameters.

POSITION        <- 1
VELOCITY        <- 2
LINEAR          <- 3
ORIENTATION     <- 4
PATHS           <- 5
COLLISIONS      <- 6

plot.names      <- c("position", "velocity", "linear", "orientation", "paths", "collisions")
plot.colors     <- c("red", "green", "blue", "gold3", "gray32", "orange")
plot.what       <- c(TRUE, TRUE, TRUE, FALSE, TRUE, FALSE)
plot.stretch    <- c(1.0, 6.0, 6.0, 8.0)

steering.names  <- c("Stop", "Continue", "Seek", "Flee", "Arrive", "Pursue", "Wander", "Follow path", "Separate", "Avoid collision")

path.1 <- path.prep(73, c(-80, -40, 40, 80), c(40, 70, 10, 40))
path.2 <- path.prep(74, c(70, 25, 45, -55, -35, -80), c(-25, -20, -80, -80, -20, -25))
Path   <- list(path.1, path.2)
paths  <- 2

# Read trajectory data from file into data frame.

trajectory.all <- read.csv(file=trajectory.file, header=FALSE, sep=",", skip=0)

# Dynamic trajectory data files have 10 fields, all relating to a single character:
#  1 time step
#  2 id
#  3 position x
#  4 position z
#  5 velocity x
#  6 velocity z
#  7 linear x
#  8 linear z
#  9 orientation
# 10 steering behavior

# Plot trajectories.
 
plot.file <- paste(work.path, "CS 330, Dynamic trajectory plot.png", sep="")

png(file=plot.file, width=1350, height=1350, res=300)  # Open plot file
par(mar=c(2.5, 3.0, 1.5, 1.0))                         # Set margins bottom, left, top, right

axis.lims  <- c(-100, 100)
axis.ticks <- seq(from=-100.0, to=100.0, by=25)

plot(NULL, xlim=axis.lims, ylim=axis.lims, xaxt="n", xaxt="n", yaxt="n", xlab="", ylab="", main="")

axis(side=1, at=axis.ticks, labels=axis.ticks,  cex.axis=0.80, mgp=c(3, 0.50, 0))
axis(side=2, at=axis.ticks, labels=-axis.ticks, cex.axis=0.80, mgp=c(3, 0.75, 0), las=1)

title.1    <- "Dynamic trajectory NE1 0.50"
title.2    <- paste(c("P", "V", "L", "O", "H", "C")[which(plot.what)], collapse="")
title.main <- paste(title.1, title.2)

title.x    <- expression(italic(x))
title.y    <- expression(italic(z))

title(main=title.main, line=0.50, cex.main=0.80)
title(xlab=title.x, line=1.25, cex.lab=1.0, family="serif")
title(ylab=title.y, line=1.50, cex.lab=1.0, family="serif")

abline(h=0, col="gray", lty="dashed")
abline(v=0, col="gray", lty="dashed")

legend("bottomright", legend=plot.names, col=plot.colors, lty=1, bty="n", cex=0.50)

if (plot.what[5]) {  # Plot paths
  for (i in 1:paths) {
    lines(type="l", x=Path[[i]]$x, y=Path[[i]]$y, col=plot.colors[PATHS], lty="dashed", lwd=1.00)
    points(x=Path[[i]]$x, y=Path[[i]]$y, pch=20, col=plot.colors[PATHS])
    text(x=Path[[i]]$x[1], y=Path[[i]]$y[1], "path", col=plot.colors[PATHS], cex=0.50, pos=ifelse(i == 1, 2, 4))
    text(x=Path[[i]]$x, y=Path[[i]]$y, num.width(Path[[i]]$param, 1, 2), col=plot.colors[PATHS], cex=0.50, pos=1)
  }
}

for (i in 1:2) {
  if (i == 1) {
    trajectory.71   <- subset(trajectory.all, trajectory.all[2] == 71)
    trajectory.81   <- subset(trajectory.all, trajectory.all[2] == 81)
    trajectory      <- rbind(trajectory.71, trajectory.81)
  } else {
    trajectory.72   <- subset(trajectory.all, trajectory.all[2] == 72)
    trajectory.82   <- subset(trajectory.all, trajectory.all[2] == 82)
    trajectory      <- rbind(trajectory.72, trajectory.82)
  }

  time.steps <- length(trajectory[,1])
  for (j in 1:time.steps) {
    if (plot.what[2]) {  # Plot character's velocity
      vel.vec.x <- c(trajectory[j, 3], trajectory[j, 3] + (trajectory[j, 5] * plot.stretch[2]))
      vel.vec.z <- c(trajectory[j, 4], trajectory[j, 4] + (trajectory[j, 6] * plot.stretch[2]))
      lines(type="l", x=vel.vec.x, y=vel.vec.z, col=plot.colors[VELOCITY], lty="solid", lwd=0.75)
    }
    if (plot.what[3]) {  # Plot character's linear acceleration
      k <- ifelse (j < time.steps, j + 1, j)
      lin.vec.x <- c(trajectory[j, 3], trajectory[j, 3] + (trajectory[k, 7] * plot.stretch[3]))
      lin.vec.z <- c(trajectory[j, 4], trajectory[j, 4] + (trajectory[k, 8] * plot.stretch[3]))
      lines(type="l", x=lin.vec.x, y=lin.vec.z, col=plot.colors[LINEAR], lty="solid", lwd=0.75)
    }
    if (plot.what[4]) {  # Plot character's orientation
      or.vec.x  <- c(trajectory[j, 3], trajectory[j, 3] + (cos(trajectory[j, 9]) * plot.stretch[4]))
      or.vec.z  <- c(trajectory[j, 4], trajectory[j, 4] + (sin(trajectory[j, 9]) * plot.stretch[4]))
      lines(type="l", x=or.vec.x, y=or.vec.z, col=plot.colors[ORIENTATION], lty="solid", lwd=0.75)
    }
  }

  if (plot.what[1]) {  # Plot character's position
    label.text <- ifelse(TRUE, steering.names[trajectory[1, 10]], "")
    lines(type="l", x=trajectory[, 3], y=trajectory[, 4], col=plot.colors[POSITION], lty="solid", lwd=1.00)
    text(x=trajectory[1, 3], y=trajectory[1, 4], label.text, col=plot.colors[POSITION], cex=0.50, pos=4)
    points(x=trajectory[1, 3], y=trajectory[1, 4], pch=20, col=plot.colors[POSITION])
  }

  if ((plot.what[6]) && (length(collisions$x) > 0)) {  # Plot collisions
    points(x=collisions$x, y=collisions$y, pch=8, col=plot.colors[COLLISIONS])
  }
}

dev.off() # Close plot file

# End of program
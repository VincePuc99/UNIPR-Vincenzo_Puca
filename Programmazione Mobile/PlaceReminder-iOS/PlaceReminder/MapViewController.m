//
//  MapViewController.m
//  PlaceReminder
//
//  Created by Vincenzo Puca on 07/06/23.
//

#import "MapViewController.h"
#import "Place.h"
#import "CustomAnnotation.h"
#include <MapKit/MapKit.h>

@implementation MapViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    [self showMapForPlace];
}

- (void)showMapForPlace {
    MKMapView *mapView = [[MKMapView alloc] initWithFrame:self.view.bounds];
    [self.view addSubview:mapView];
    
    mapView.delegate = self;
    
    for (Place *place in self.places) {
        CustomAnnotation *annotation = [[CustomAnnotation alloc] init];
        annotation.coordinate = CLLocationCoordinate2DMake(place.latitude, place.longitude);
        annotation.title = place.customName;
        annotation.subtitle = place.name;
        annotation.place = place;
        
        [mapView addAnnotation:annotation];
    }
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    if ([annotation isKindOfClass:[MKUserLocation class]]) {
        return nil;
    }
    
    static NSString *identifier = @"CustomAnnotation";
    MKMarkerAnnotationView *annotationView = (MKMarkerAnnotationView *)[mapView dequeueReusableAnnotationViewWithIdentifier:identifier];
    
    if (annotationView == nil) {
        annotationView = [[MKMarkerAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:identifier];
    } else {
        annotationView.annotation = annotation;
    }
    
    annotationView.canShowCallout = YES;
    annotationView.calloutOffset = CGPointMake(-5, 5);
    annotationView.markerTintColor = [UIColor yellowColor];
    
    UIButton *rightButton = [UIButton buttonWithType:UIButtonTypeDetailDisclosure];
    annotationView.rightCalloutAccessoryView = rightButton;
    
    return annotationView;
}

- (void)mapView:(MKMapView *)mapView annotationView:(MKAnnotationView *)view calloutAccessoryControlTapped:(UIControl *)control {
    if ([view.annotation isKindOfClass:[CustomAnnotation class]]) {
        CustomAnnotation *customAnnotation = (CustomAnnotation *)view.annotation;
        Place *selectedPlace = customAnnotation.place;
        
        NSString *placeInfo = [NSString stringWithFormat:@"Name: %@\nAddress: %@\nDescription: %@\nDate: %@\nCoordinates: (%f, %f)", selectedPlace.customName, selectedPlace.name, selectedPlace.descriptionText, selectedPlace.dateAdded, selectedPlace.latitude, selectedPlace.longitude];
        
        UIAlertController *alertController = [UIAlertController alertControllerWithTitle:@"Place Information" message:placeInfo preferredStyle:UIAlertControllerStyleAlert];
        [alertController addAction:[UIAlertAction actionWithTitle:@"OK" style:UIAlertActionStyleDefault handler:nil]];
        [self presentViewController:alertController animated:YES completion:nil];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];

    [self.navigationController setNavigationBarHidden:NO animated:animated];
}

@end

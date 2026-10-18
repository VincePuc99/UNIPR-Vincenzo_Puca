//
//  MapViewController.h
//  PlaceReminder
//
//  Created by Vincenzo Puca on 07/06/23.
//

#import <UIKit/UIKit.h>
#import <MapKit/MapKit.h>
#import "CustomAnnotation.h"
#import "Place.h"

@interface MapViewController : UIViewController <MKMapViewDelegate>

@property (nonatomic, strong) NSMutableArray<Place *> *places;

@property (strong, nonatomic) IBOutlet UIView *MapViewController;

@end
